package com.sven.web.configuration.intercepter

import com.alibaba.fastjson.JSON
import com.sven.web.configuration.entity.ResponseData
import com.sven.web.util.error.CustomError
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.core.annotation.Order
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@Aspect
@Order(1)
class ResponseResultInterceptor {
    private val logger = LoggerFactory.getLogger(ResponseResultInterceptor::class.java)

    @Around("execution (* com.sven.web.controller.**.*(..))")
    fun responseResult(joinPoint: ProceedingJoinPoint): ResponseData? {
        val requestAttributes = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        val request = requestAttributes.request
        val response = requestAttributes.response!!
        logRequestIncoming(request)
        val startTime = System.currentTimeMillis()
        val responseData = ResponseData(timestamp = Date())
        try {
            val result = joinPoint.proceed(joinPoint.args)
            responseData.status = "OK"
            responseData.data = result
            return responseData
        } catch (e: Exception) {
            var status = HttpStatus.INTERNAL_SERVER_ERROR.value()
            if (e is CustomError) {
                logger.warn("{}: {}", e.javaClass.simpleName, e.message)
                status = e.statusCode
                responseData.error = e.code
            } else {
                responseData.error = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase
                logger.error("", e)
            }
            responseData.status = "ERROR"
            responseData.message = e.message
            writeResponse(response, responseData, status)
        }
        // return null的时候，就不会再调用后面的interceptor并且不会再response.write了
        val ms = System.currentTimeMillis() - startTime
        logResponseOuting(request, response, ms)
        return null
    }

    private fun logRequestIncoming(request: HttpServletRequest) {
        var path = request.servletPath
        if (!request.queryString.isNullOrEmpty()) {
            path += request.queryString
        }
        logger.info("{} {}", request.method, path)
    }

    private fun logResponseOuting(request: HttpServletRequest, response: HttpServletResponse, ms: Long) {
        var path = request.servletPath
        if (!request.queryString.isNullOrEmpty()) {
            path += request.queryString
        }
        logger.info("{} {} {} {}ms", request.method, path, response.status, ms)
    }

    private fun writeResponse(response: HttpServletResponse, data: Any, status: Int = HttpStatus.INTERNAL_SERVER_ERROR.value()) {
        val writer = response.writer
        response.status = status
        response.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
        writer.write(JSON.toJSONString(data))
        writer.flush()
        writer.close()
    }
}
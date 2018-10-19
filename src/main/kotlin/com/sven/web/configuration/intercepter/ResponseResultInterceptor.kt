package com.sven.web.configuration.intercepter

import com.alibaba.fastjson.JSON
import com.sven.web.common.error.service.BaseServiceError
import com.sven.web.configuration.entity.ApiErrorResponse
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
import javax.servlet.http.HttpServletResponse

/**
 * interceptor中的异常无法被GlobalExceptionHandler捕获，因此需要在这里处理
 */
@Aspect
@Order(2)
@Component
class ResponseResultInterceptor {
    private val logger = LoggerFactory.getLogger(ResponseResultInterceptor::class.java)

    @Around("execution (* com.sven.web.controller.**.*(..))")
    fun responseResult(joinPoint: ProceedingJoinPoint): ApiErrorResponse? {
        val requestAttributes = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        val response = requestAttributes.response!!
        val responseData = ApiErrorResponse(timestamp = Date())
        try {
            val result = joinPoint.proceed(joinPoint.args)
            responseData.status = "OK"
            responseData.data = result
            return responseData
        } catch (e: Exception) {
            var status = HttpStatus.INTERNAL_SERVER_ERROR.value()
            if (e is BaseServiceError) {
                logger.warn("{}: {}", e.javaClass.name, e.message)
                status = e.statusCode
                responseData.error = e.code
            } else {
                responseData.error = HttpStatus.INTERNAL_SERVER_ERROR.name
                logger.error("", e)
            }
            responseData.status = "ERROR"
            responseData.message = e.message
            writeResponseError(response, responseData, status)
        }
        // return null的时候，就不会再调用后面的interceptor并且不会再response.write了
        return null
    }

    /**
     * 请求异常无法被GlobalExceptionHandler捕获，因此在这里直接向response写入错误数据
     */
    private fun writeResponseError(response: HttpServletResponse, data: Any, status: Int = HttpStatus.INTERNAL_SERVER_ERROR.value()) {
        val writer = response.writer
        response.status = status
        response.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
        writer.write(JSON.toJSONString(data))
        writer.flush()
        writer.close()
    }
}
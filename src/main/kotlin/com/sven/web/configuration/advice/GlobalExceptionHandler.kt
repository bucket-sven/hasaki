package com.sven.web.configuration.advice

import com.sven.web.configuration.entity.ResponseData
import com.sven.web.util.error.CustomError
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 此Handler无法捕获aop中的异常，目前用于捕获其他异常，比如Resolver中的异常
 */
@RestControllerAdvice
class GlobalExceptionHandler {
    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(value = [ CustomError::class, Exception::class ])
    @ResponseBody
    fun defaultErrorHandler(req: HttpServletRequest, res: HttpServletResponse, e: Exception): ResponseData {
        val responseData = ResponseData(timestamp = Date(), message = e.message)
        val status: Int
        if (e is CustomError) {
            logger.warn("{}: {}", e.javaClass.simpleName, e.message)
            responseData.error = HttpStatus.BAD_REQUEST.reasonPhrase
            status = e.statusCode
        } else {
            logger.error("", e)
            responseData.error = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase
            status = HttpStatus.INTERNAL_SERVER_ERROR.value()
        }
        res.status = status
        responseData.status = "ERROR"
        return responseData
    }
}
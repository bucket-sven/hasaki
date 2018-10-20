package com.sven.web.controller.advice

import com.sven.web.common.error.framework.BaseFrameworkError
import com.sven.web.common.error.service.BaseServiceError
import com.sven.web.service.model.ApiResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.validation.BindException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 用于捕获非业务性异常，比如Resolver中的异常
 */
@RestControllerAdvice
class GlobalExceptionHandler {
    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(value = [ BaseFrameworkError::class ])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun defaultErrorHandler(req: HttpServletRequest, res: HttpServletResponse, e: BaseFrameworkError): ApiResponse {
        val responseData = ApiResponse(timestamp = Date(), message = e.message)
        logger.warn("{}: {}", e.javaClass.name, e.message)
        responseData.error = e.code
        res.status = e.statusCode
        responseData.status = "ERROR"
        return responseData
    }

    @ExceptionHandler(value = [ NoHandlerFoundException::class ])
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun notFound(): ApiResponse {
        val status = HttpStatus.NOT_FOUND
        val responseData = ApiResponse(timestamp = Date(), error = status.name, message = status.reasonPhrase)
        responseData.status = "ERROR"
        return responseData
    }

    @ExceptionHandler(value = [ Exception::class ])
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun internalServerError(e: Exception): ApiResponse {
        logger.error("", e)
        val status = HttpStatus.INTERNAL_SERVER_ERROR
        val responseData = ApiResponse(timestamp = Date(), error = status.name, message = status.reasonPhrase)
        responseData.status = "ERROR"
        return responseData
    }

    @ExceptionHandler(value = [ HttpMediaTypeNotSupportedException::class ])
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    fun unsupportedMediaType(e: Exception): ApiResponse {
        logger.warn(e.message)
        val status = HttpStatus.UNSUPPORTED_MEDIA_TYPE
        val responseData = ApiResponse(timestamp = Date(), error = status.name, message = status.reasonPhrase)
        responseData.status = "ERROR"
        return responseData
    }

    @ExceptionHandler(value = [ BindException::class ])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun invalidParams(e: BindException): ApiResponse {
        val messages = e.fieldErrors.map { "${it.field} ${it.defaultMessage}" }
        val responseData = ApiResponse(timestamp = Date(), error = "params_error", message = messages.joinToString(","))
        responseData.status = "ERROR"
        return responseData
    }

    @ExceptionHandler(value = [ BaseServiceError::class ])
    fun serviceError(e: BaseServiceError, res: HttpServletResponse): ApiResponse {
        val responseData = ApiResponse(timestamp = Date(), error = e.code, message = e.message)
        res.status = e.statusCode
        logger.error("{}", e)
        responseData.status = "ERROR"
        responseData.message = e.message
        return responseData
    }
}
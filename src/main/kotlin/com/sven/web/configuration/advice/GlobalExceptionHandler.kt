package com.sven.web.configuration.advice

import com.sven.web.util.error.CustomError
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ResponseData(var timestamp: Date? = null,
                   var status: Int? = null,
                   var error: String? = null,
                   var message: String? = null,
                   var path: String? = null)

@RestControllerAdvice
class GlobalExceptionHandler {
    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(value = [ CustomError::class, Exception::class ])
    @ResponseBody
    fun defaultErrorHandler(req: HttpServletRequest, res: HttpServletResponse, e: Exception): ResponseData {
        logger.error("", e)
        val responseData = ResponseData(timestamp = Date(), path = req.servletPath, message = e.message)
        val status: Int
        if (e is CustomError) {
            responseData.error = HttpStatus.BAD_REQUEST.reasonPhrase
            status = e.statusCode
        } else {
            responseData.error = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase
            status = HttpStatus.INTERNAL_SERVER_ERROR.value()
        }
        res.status = status
        responseData.status = status
        return responseData
    }
}
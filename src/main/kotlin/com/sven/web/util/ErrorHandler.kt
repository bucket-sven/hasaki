package com.sven.web.util

import com.sven.web.util.error.CustomError
import org.springframework.http.HttpStatus

object ErrorHandler {
    fun requireParams(name: String, statusCode: Int = HttpStatus.BAD_REQUEST.value()) {
        throw CustomError("$name required", statusCode)
    }
}
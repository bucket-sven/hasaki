package com.sven.web.util.error

open class CustomError(val code: String, message: String, val statusCode: Int) : Exception(message)

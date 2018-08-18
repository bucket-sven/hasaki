package com.sven.web.common.error

open class BaseError(val code: String, message: String, val statusCode: Int) : Exception(message)
package com.sven.web.util.error

class CustomError(message: String, var statusCode: Int) : Exception(message)
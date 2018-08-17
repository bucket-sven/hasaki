package com.sven.web.util.error

import org.springframework.http.HttpStatus

class ParamsError(code: String, message: String, statusCode: Int = HttpStatus.BAD_REQUEST.value()) : CustomError(code, message, statusCode)
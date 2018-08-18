package com.sven.web.common.error.framework

import org.springframework.http.HttpStatus

class ParamsError(code: String, message: String, statusCode: Int = HttpStatus.BAD_REQUEST.value()) : BaseFrameworkError(code, message, statusCode)
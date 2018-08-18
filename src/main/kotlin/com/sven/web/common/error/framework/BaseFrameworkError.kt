package com.sven.web.common.error.framework

import com.sven.web.common.error.BaseError
import org.springframework.http.HttpStatus

/**
 * 框架层异常，在GlobalExceptionHandler中处理
 */
open class BaseFrameworkError(code: String, message: String, statusCode: Int = HttpStatus.BAD_REQUEST.value()) : BaseError(code, message, statusCode)

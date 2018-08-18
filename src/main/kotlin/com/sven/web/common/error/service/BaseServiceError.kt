package com.sven.web.common.error.service

import com.sven.web.common.error.BaseError
import org.springframework.http.HttpStatus

/**
 * 业务层异常，在interceptor中处理
 */
open class BaseServiceError(code: String, message: String, statusCode: Int = HttpStatus.BAD_REQUEST.value()) : BaseError(code, message, statusCode)

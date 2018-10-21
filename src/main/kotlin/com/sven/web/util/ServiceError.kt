package com.sven.web.util

import com.sven.web.common.error.service.BaseServiceError

fun raise(code: String, message: String = "", statusCode: Int = 400) {
    throw BaseServiceError(code, message, statusCode)
}

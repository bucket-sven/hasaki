package com.sven.web.service

open class BaseListParams(page: Int?, count: Int?) {
    var page = page ?: 1
    var count = count ?: 20
}
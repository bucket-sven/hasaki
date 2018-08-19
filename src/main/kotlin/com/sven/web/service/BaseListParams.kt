package com.sven.web.service

//import javax.validation.constraints.NotNull

open class BaseListParams(page: Int = 1, count: Int = 20) {
//    @NotNull(message = "不能为空")
    var page = page
    var count = count
}
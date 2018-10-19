package com.sven.web.service.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

//import javax.validation.constraints.NotNull

@JsonIgnoreProperties(ignoreUnknown = true)
open class BaseListParams(page: Int, count: Int) {
//    @NotNull(message = "不能为空")
    var page = page
        get() = if (field <= 0) 1 else field

    var count = count
        get() = if (field <= 0) 20 else field
}
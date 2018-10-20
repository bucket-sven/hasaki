package com.sven.web.service.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

//import javax.validation.constraints.NotNull

@JsonIgnoreProperties(ignoreUnknown = true)
open class BaseListParams(page: Int, count: Int) {
//    @NotNull(message = "不能为空")
    // JPA 分页从0开始， MyBatis则从1
    var page = page
        get() = if (field < 1) 0 else field - 1

    var count = count
        get() = if (field <= 0) 20 else field
}
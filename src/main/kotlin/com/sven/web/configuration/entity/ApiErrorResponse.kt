package com.sven.web.configuration.entity

import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.annotation.JsonInclude
import com.sven.web.util.Constants
import java.util.*

/**
 * 统一接口返回数据格式
 */
// NON_NULL也可以用于修饰单个字段，修饰类时，对所有字段生效
@JsonInclude(JsonInclude.Include.NON_NULL)
class ApiErrorResponse(timestamp: Date? = null,
                       var status: String? = null,
                       var error: String? = null,
                       var message: String? = null,
                       var data: Any? = null) {
    @JSONField(format = Constants.DATE_FORMAT)
    var timestamp: Date? = timestamp
}

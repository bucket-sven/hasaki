package com.sven.web.configuration.entity

import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.annotation.JsonInclude
import com.sven.web.util.Constants
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
class ResponseData(timestamp: Date? = null,
                   var status: String? = null,
                   var error: String? = null,
                   var message: String? = null,
                   var data: Any? = null) {
    @JSONField(format = Constants.DATE_FORMAT)
    var timestamp: Date? = timestamp
}

package com.sven.web.configuration.entity

import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
class ResponseData(var timestamp: Date? = null,
                   var status: String? = null,
                   var error: String? = null,
                   var message: String? = null,
                   var data: Any? = null)

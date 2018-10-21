package com.sven.web.service.model

import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

/**
 * 统一接口返回数据格式
 */
// @JSONField(format = Constants.DATE_FORMAT)
// NON_NULL也可以用于修饰单个字段，修饰类时，对所有字段生效
@JsonInclude(JsonInclude.Include.NON_NULL)
class ApiResponse(var timestamp: Date? = null,
                  var status: String? = null,
                  var error: String? = null,
                  var message: String? = null,
                  var data: Any? = null)

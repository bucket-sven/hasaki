package com.sven.web.service.model

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotEmpty

// 注解不能出现在构造函数的参数上, 否则validator不生效
class AuthParams(regType: String?, account: String?) {
    @Length(min = 2, max = 10)
    @NotEmpty
    var account = account

    @NotEmpty
    var regType = regType
}
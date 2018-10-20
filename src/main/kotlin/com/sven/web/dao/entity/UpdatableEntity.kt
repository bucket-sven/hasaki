package com.sven.web.dao.entity

import com.alibaba.fastjson.annotation.JSONField
import com.sven.web.util.Constants
import org.springframework.data.annotation.LastModifiedDate
import java.util.*
import javax.persistence.Column
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class UpdatableEntity : BaseEntity() {
    @Column
    @LastModifiedDate
    @JSONField(format = Constants.DATE_FORMAT)
    var updateTime: Date? = null
}
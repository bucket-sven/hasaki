package com.sven.web.dao.entity

import com.alibaba.fastjson.annotation.JSONField
import com.sven.web.util.Constants
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.util.*
import javax.persistence.*

@MappedSuperclass
open class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column
    @CreatedDate
    @JSONField(format = Constants.DATE_FORMAT)
    var createTime: Date? = null

    @Column
    @LastModifiedDate
    @JSONField(format = Constants.DATE_FORMAT)
    var updateTime: Date? = null
}

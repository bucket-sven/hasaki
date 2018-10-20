package com.sven.web.dao.entity

import com.alibaba.fastjson.annotation.JSONField
import com.sven.web.util.Constants
import org.springframework.data.annotation.CreatedDate
import java.util.*
import javax.persistence.*

@MappedSuperclass
abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column
    @CreatedDate
    @JSONField(format = Constants.DATE_FORMAT)
    var createTime: Date? = null
}

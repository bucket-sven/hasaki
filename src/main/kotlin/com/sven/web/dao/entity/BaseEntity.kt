package com.sven.web.dao.entity

import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedDate
import javax.persistence.*

@MappedSuperclass
open class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column
    @CreatedDate
    var createTime: String? = null

    @Column
    @UpdateTimestamp
    var updateTime: String? = null
}

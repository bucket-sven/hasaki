package com.sven.web.dao.entity

import javax.persistence.Column
import javax.persistence.Entity

@Entity
class User : BaseEntity() {
    @Column
    var account: String? = null

    @Column
    var regType: String? = null
}
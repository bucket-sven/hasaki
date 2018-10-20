package com.sven.web.dao.entity

import javax.persistence.Column
import javax.persistence.Entity

@Entity
class User : UpdatableEntity() {
    @Column
    var account: String? = null

    @Column
    var regType: String? = null
}
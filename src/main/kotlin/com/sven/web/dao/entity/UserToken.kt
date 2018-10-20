package com.sven.web.dao.entity

import javax.persistence.Column
import javax.persistence.Entity

@Entity
class UserToken : UpdatableEntity() {
    @Column
    var token: String? = null

    @Column
    var userId: Long? = null
}
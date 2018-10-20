package com.sven.web.dao.repository

import com.sven.web.dao.entity.UserToken
import org.springframework.data.repository.PagingAndSortingRepository

interface UserTokenRepository : PagingAndSortingRepository<UserToken, Long> {
    fun findByUserId(userId: Long?): UserToken?
}
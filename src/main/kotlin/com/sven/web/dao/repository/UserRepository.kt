package com.sven.web.dao.repository

import com.sven.web.dao.entity.User
import org.springframework.data.repository.PagingAndSortingRepository

interface UserRepository : PagingAndSortingRepository<User, Long> {
    fun findByAccountAndRegType(account: String?, regType: String?): User?
}
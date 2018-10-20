package com.sven.web.controller

import com.alibaba.fastjson.JSON
import com.sven.web.configuration.multicontenttype.CustomRequestParams
import com.sven.web.dao.repository.UserRepository
import com.sven.web.service.AuthService
import com.sven.web.service.model.AuthParams
import com.sven.web.service.model.BaseListParams
import com.sven.web.util.RedisUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
class AuthController {
    @Autowired
    private lateinit var authService: AuthService
    @Autowired
    private lateinit var mainRedisUtil: RedisUtil

    @Autowired
    private lateinit var userRepository: UserRepository

    @RequestMapping("/auth/sign", method = [ RequestMethod.POST ], consumes = [ MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE ])
    fun sign(@CustomRequestParams params: AuthParams): Any? {
        return authService.auth(params)
    }

    @RequestMapping("/user/list")
    fun users(@CustomRequestParams params: BaseListParams): Any? {
        val key = "user_list:${params.page}:${params.count}"
        return mainRedisUtil.fetchArray(key, expireSeconds = 30) {
            val info = userRepository.findAll(PageRequest.of(params.page, params.count))
            info.content
        }
    }

    @RequestMapping("/user")
    fun customUserList(@CustomRequestParams params: AuthParams): Any? {
        println(JSON.toJSONString(params))
        return params
    }
}
package com.sven.web.controller

import com.alibaba.fastjson.JSON
import com.baomidou.mybatisplus.mapper.EntityWrapper
import com.baomidou.mybatisplus.plugins.Page
import com.sven.web.configuration.multicontenttype.CustomRequestParams
import com.sven.web.dao.entity.User
import com.sven.web.dao.mapper.UserMapper
import com.sven.web.service.Auth
import com.sven.web.service.AuthService
import com.sven.web.service.BaseListParams
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
class AuthController {
    @Autowired
    private lateinit var authService: AuthService

    @Autowired
    private lateinit var userMapper: UserMapper

    @RequestMapping("/auth/sign", method = [ RequestMethod.POST ], consumes = [ MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE ])
    fun sign(@CustomRequestParams params: Auth): Any? {
        val auth = Auth(account = params.account, regType = params.regType)
        return authService.auth(auth)
    }

    @RequestMapping("/user/list")
    fun users(@CustomRequestParams params: BaseListParams): Any? {
        val wrapper = EntityWrapper(User())
        val pageObj = Page<User>(params.page, params.count)
        wrapper.orderBy("id DESC")
        return userMapper.selectPage(pageObj, wrapper)
    }

    @RequestMapping("/user")
    fun customUserList(@CustomRequestParams params: Auth): Any? {
        println(JSON.toJSONString(params))
        return null
    }
}
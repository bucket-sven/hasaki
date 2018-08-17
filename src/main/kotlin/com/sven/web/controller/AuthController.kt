package com.sven.web.controller

import com.baomidou.mybatisplus.mapper.EntityWrapper
import com.baomidou.mybatisplus.plugins.Page
import com.sven.web.configuration.multicontenttype.CustomRequestParams
import com.sven.web.dao.entity.User
import com.sven.web.dao.mapper.UserMapper
import com.sven.web.service.Auth
import com.sven.web.service.AuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class AuthController {
    @Autowired
    private lateinit var authService: AuthService

    @Autowired
    private lateinit var userMapper: UserMapper

    @RequestMapping("/auth/sign", method = [ RequestMethod.POST ], consumes = [ MediaType.ALL_VALUE ])
    fun sign(@CustomRequestParams @Valid params: Auth): Any? {
        val auth = Auth(account = params.account, regType = params.regType)
        return authService.auth(auth)
    }

    @RequestMapping("/auth/test")
    fun sign(@CustomRequestParams map: HashMap<String, String>): Any {
        val wrapper = EntityWrapper<User>(User())
        val page = Page<User>(1, 10)
        wrapper.where("account={0}", "test").orderBy("id DESC")
        return userMapper.selectPage(page, wrapper)
    }
}
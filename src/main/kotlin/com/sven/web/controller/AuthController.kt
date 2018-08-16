package com.sven.web.controller

import com.baomidou.mybatisplus.mapper.EntityWrapper
import com.baomidou.mybatisplus.plugins.Page
import com.sven.web.configuration.multicontenttype.CustomRequestParams
import com.sven.web.dao.entity.User
import com.sven.web.dao.mapper.UserMapper
import com.sven.web.service.Auth
import com.sven.web.service.AuthService
import com.sven.web.util.ErrorHandler
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
    fun sign(@CustomRequestParams @Valid params: Auth): Any {
        if (params.account.isNullOrBlank()) {
            ErrorHandler.requireParams("account")
        }
        val auth = Auth(account = params.account, regType = params.regType)
        val data = authService.auth(auth)
        return hashMapOf(
                "status" to "OK",
                "data" to data
        )
    }

    @RequestMapping("/auth/test")
    fun sign(@CustomRequestParams map: HashMap<String, String>): Any {
        val wrapper = EntityWrapper<User>(User())
        val page = Page<User>(1, 10)
        wrapper.where("account={0}", "test").orderBy("id DESC")
        val resp = userMapper.selectPage(page, wrapper)
        return resp
    }
}
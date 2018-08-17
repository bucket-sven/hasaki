package com.sven.web.service

import cn.hutool.core.util.RandomUtil
import com.sven.web.dao.entity.User
import com.sven.web.dao.entity.UserToken
import com.sven.web.dao.mapper.UserMapper
import com.sven.web.dao.mapper.UserTokenMapper
import org.hibernate.validator.constraints.Length
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.validation.constraints.NotEmpty

//import java.util.concurrent.TimeUnit

// 注解不能出现在构造函数的参数上, 否则validator不生效
class Auth(var regType: String?, account: String?) {
    @Length(min = 2, max = 10)
    @NotEmpty
    var account: String? = account
}

@Service
class AuthService {
    @Autowired
    private lateinit var userDAO: UserMapper
    @Autowired
    private lateinit var userTokenDAO: UserTokenMapper

    @Autowired
    @Qualifier("mainRedis")
    private lateinit var mainRedis: StringRedisTemplate

    @Autowired
    @Qualifier("liveRedis")
    private lateinit var liveRedis: StringRedisTemplate

    companion object {
        private const val USER_PREFIX = "user:"
        private const val TOKEN_PREFIX = "token:"
    }

    @Transactional
    fun auth(data: Auth): User? {
        val users = userDAO.selectByMap(hashMapOf<String, Any?>("account" to data.account, "reg_type" to data.regType))
        val user: User
        if (users.size == 0) {
            user = User(account = data.account, regType = data.regType)
            userDAO.insert(user)
        } else {
            user = users[0]
        }
        val token = RandomUtil.randomString(32)
        var userToken = userTokenDAO.selectOne(UserToken(userId= user.id))
        if (userToken == null) {
            userToken = UserToken(token = token, userId = user.id)
            userTokenDAO.insert(userToken)
        } else {
            userToken.token = token
            userTokenDAO.updateById(userToken)
        }
        mainRedis.execute {
            it.multi()
            it.setEx((USER_PREFIX + user.id).toByteArray(), 60, token.toByteArray())
            it.setEx((TOKEN_PREFIX + token).toByteArray(), 60, user.id.toString().toByteArray())
            it.exec()
        }
//        liveRedis.opsForValue().set("test", "value", 1, TimeUnit.MINUTES)
        return user
    }
}
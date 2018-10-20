package com.sven.web.service

import cn.hutool.core.util.RandomUtil
import com.sven.web.dao.repository.UserRepository
import com.sven.web.dao.repository.UserTokenRepository
import com.sven.web.dao.entity.User
import com.sven.web.dao.entity.UserToken
import com.sven.web.service.model.AuthParams
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
//import java.util.concurrent.TimeUnit

@Service
class AuthService {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var userTokenRepository: UserTokenRepository

    @Autowired
    @Qualifier("mainRedis")
    private lateinit var mainRedis: StringRedisTemplate

//    @Autowired
//    @Qualifier("liveRedis")
//    private lateinit var liveRedis: StringRedisTemplate

    companion object {
        private const val USER_PREFIX = "user:"
        private const val TOKEN_PREFIX = "token:"
    }

    @Transactional
    fun auth(data: AuthParams): User? {
        var user = userRepository.findByAccountAndRegType(data.account, data.regType)
        if (user == null) {
            val t = User()
            t.account = data.account
            t.regType = data.regType
            user = userRepository.save(t)
        }

        val token = RandomUtil.randomString(32)
        var userToken = userTokenRepository.findByUserId(user.id)
        if (userToken == null) {
            userToken = UserToken()
            userToken.token = token
            userToken.userId = user.id
            userTokenRepository.save(userToken)
        } else {
            userToken.token = token
            userTokenRepository.save(userToken)
        }

        mainRedis.execute {
            val key1 = (USER_PREFIX + user.id).toByteArray()
            val key2 = (TOKEN_PREFIX + token).toByteArray()
            it.multi()
            it.setEx(key1, 60, token.toByteArray())
            it.setEx(key2, 60, user.id.toString().toByteArray())
            it.exec()
        }
//        liveRedis.opsForValue().set("test", "value", 1, TimeUnit.MINUTES)
        return user
    }
}
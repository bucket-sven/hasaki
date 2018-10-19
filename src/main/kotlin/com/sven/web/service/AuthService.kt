package com.sven.web.service

import cn.hutool.core.util.RandomUtil
import com.sven.web.dao.entity.User
import com.sven.web.dao.entity.UserToken
import com.sven.web.dao.mapper.UserMapper
import com.sven.web.dao.mapper.UserTokenMapper
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
    private lateinit var userMapper: UserMapper
    @Autowired
    private lateinit var userTokenMapper: UserTokenMapper

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
        val users = userMapper.selectByMap(hashMapOf<String, Any?>("account" to data.account, "reg_type" to data.regType))
        val user: User
        if (users.size == 0) {
            user = User(account = data.account, regType = data.regType)
            userMapper.insert(user)
        } else {
            user = users[0]
        }

        val token = RandomUtil.randomString(32)
        var userToken = userTokenMapper.selectOne(UserToken(userId= user.id))
        if (userToken == null) {
            userToken = UserToken(token = token, userId = user.id)
            userTokenMapper.insert(userToken)
        } else {
            userToken.token = token
            userTokenMapper.updateById(userToken)
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
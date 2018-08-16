package com.sven.web.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate

open class BaseRedisConfig {
    var host: String = "localhost"
    var port: Int = 6379
    var database: Int = 0
}

@Configuration
@ConfigurationProperties(prefix = "spring.redis.main")
class MainRedisConfig : BaseRedisConfig()

@Configuration
@ConfigurationProperties(prefix = "spring.redis.live")
class LiveRedisConfig : BaseRedisConfig()

@Configuration
class RedisConfig {
    @Bean
    fun mainRedis(redisConfig: MainRedisConfig): StringRedisTemplate {
        return createRedisClient(redisConfig)
    }

    @Bean
    fun liveRedis(redisConfig: LiveRedisConfig): StringRedisTemplate {
        return createRedisClient(redisConfig)
    }

    private fun createRedisClient(redisConfig: BaseRedisConfig): StringRedisTemplate {
        val redisTemplate = StringRedisTemplate()
        val conf = RedisStandaloneConfiguration()
        val jedis = JedisConnectionFactory(conf)
        jedis.poolConfig?.minIdle = 2
//        jedis.poolConfig?.maxTotal = 10
//        jedis.poolConfig?.maxIdle = 8
        conf.hostName = redisConfig.host
        conf.port = redisConfig.port
        conf.database = redisConfig.database
        redisTemplate.setConnectionFactory(jedis)
        return redisTemplate
    }
}
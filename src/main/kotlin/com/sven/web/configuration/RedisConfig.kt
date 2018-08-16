package com.sven.web.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate

class BaseRedisConfig : RedisStandaloneConfiguration()

@Configuration
class RedisConfig {
    @Bean
    @ConfigurationProperties("spring.redis.main")
    fun mainRedisConfig(): BaseRedisConfig {
        return BaseRedisConfig()
    }

    @Bean
    @ConfigurationProperties("spring.redis.live")
    fun liveRedisConfig(): BaseRedisConfig {
        return BaseRedisConfig()
    }

    @Bean
    fun mainRedis(): StringRedisTemplate {
        return createRedisClient(mainRedisConfig())
    }

    @Bean
    fun liveRedis(): StringRedisTemplate {
        return createRedisClient(liveRedisConfig())
    }

    private fun createRedisClient(redisConfig: BaseRedisConfig): StringRedisTemplate {
        val redisTemplate = StringRedisTemplate()
        val jedis = JedisConnectionFactory(redisConfig)
        jedis.poolConfig?.minIdle = 2
//        jedis.poolConfig?.maxTotal = 10
//        jedis.poolConfig?.maxIdle = 8
        redisTemplate.setConnectionFactory(jedis)
        return redisTemplate
    }
}
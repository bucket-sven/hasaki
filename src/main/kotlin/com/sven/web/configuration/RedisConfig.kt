package com.sven.web.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate

@Configuration
class RedisConfig {
    @Bean
    fun mainRedis(@Value("\${spring.redis.main.host:localhost}")hostName: String,
                  @Value("\${spring.redis.main.port:6379}")port: Int,
                  @Value("\${spring.redis.main.database:0}")database: Int
    ): StringRedisTemplate {
        val redisTemplate = StringRedisTemplate()
        val jedis = JedisConnectionFactory()
        val conf = RedisStandaloneConfiguration()
        conf.hostName = hostName
        conf.port = port
        conf.database = database
        redisTemplate.setConnectionFactory(jedis)
        return redisTemplate
    }

    @Bean
    fun liveRedis(@Value("\${spring.redis.live.host:localhost}")hostName: String,
                  @Value("\${spring.redis.live.port:6379}")port: Int,
                  @Value("\${spring.redis.live.database:0}")database: Int
    ): StringRedisTemplate {
        val redisTemplate = StringRedisTemplate()
        val conf = RedisStandaloneConfiguration()
        val jedis = JedisConnectionFactory(conf)
        conf.hostName = hostName
        conf.port = port
        conf.database = database
        redisTemplate.setConnectionFactory(jedis)
        return redisTemplate
    }
}
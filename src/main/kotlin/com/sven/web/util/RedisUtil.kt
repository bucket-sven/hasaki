package com.sven.web.util

import com.alibaba.fastjson.JSON
import org.springframework.data.redis.core.RedisOperations
import org.springframework.data.redis.core.RedisTemplate
import java.util.*
import java.util.concurrent.TimeUnit

class RedisUtil(var redisTemplate: RedisTemplate<String, String>) : RedisOperations<String, String> by redisTemplate {
//    /**
//     * @param key 缓存的key
//     * @param expireSeconds 超时时间
//     * @param expireSecondsWhenNull 当返回值为空时，防止缓存穿透时间
//     * @param func 回调函数
//     */
//    inline fun <reified T>fetch(key: String, expireSeconds: Long = 300, expireSecondsWhenNull: Long = 15, func: () -> T): T? {
//        val str = redisTemplate.opsForValue().get(key)
//        if (str == null) {
//            val res = func()
//            if (expireSeconds == 0L) {
//                redisTemplate.opsForValue().set(key, JSON.toJSONString(res))
//            } else {
//                var timeout = expireSeconds
//                if (res == null || (res is String && res.trim() == "")) {
//                    timeout = expireSecondsWhenNull
//                }
//                redisTemplate.opsForValue().set(key, JSON.toJSONString(res), timeout, TimeUnit.SECONDS)
//            }
//            return res
//        }
//        return JSON.parseObject(str, T::class.java)
//    }

    /**
     * 缓存普通对象, 不能存储集合、Optional对象
     * @param key 缓存的key
     * @param expireSeconds 超时时间
     * @param expireSecondsWhenNull 当返回值为空时，防止缓存穿透时间
     * @param func 回调函数
     */
    inline fun <reified T>fetchObject(key: String, expireSeconds: Long = 300, expireSecondsWhenNull: Long = 15, func: () -> T?): T? {
        val ops = redisTemplate.opsForValue()
        val str = ops.get(key)
        if (str == null) {
            val data = func()
            var expires = expireSeconds
            if (data == null) {
                expires = expireSecondsWhenNull
            }
            ops.set(key, JSON.toJSONString(data), expires, TimeUnit.SECONDS)
            return data
        }
        return JSON.parseObject(str, T::class.java)
    }

    /**
     * 缓存Optional对象
     * @param key 缓存的key
     * @param expireSeconds 超时时间
     * @param expireSecondsWhenNull 当返回值为空时，防止缓存穿透时间
     * @param func 回调函数
     */
    inline fun <reified T>fetchOptional(key: String, expireSeconds: Long = 300, expireSecondsWhenNull: Long = 15, func: () -> Optional<T>): T? {
        val ops = redisTemplate.opsForValue()
        val str = ops.get(key)
        if (str == null) {
            val data = func()
            val expires = if (!data.isPresent) expireSecondsWhenNull else expireSeconds
            ops.set(key, JSON.toJSONString(data), expires, TimeUnit.SECONDS)
            if (data.isPresent) {
                return data.get()
            }
            return null
        }
        return JSON.parseObject(str, T::class.java)
    }

    /**
     * 缓存集合对象
     * @param key 缓存的key
     * @param expireSeconds 超时时间
     * @param func 回调函数
     */
    inline fun <reified T>fetchArray(key: String, expireSeconds: Long = 300, func: () -> Iterable<T>): Iterable<T> {
        val ops = redisTemplate.opsForValue()
        val str = ops.get(key)
        if (str == null) {
            val data = func()
            ops.set(key, JSON.toJSONString(data), expireSeconds, TimeUnit.SECONDS)
            return data
        }
        return JSON.parseArray(str, T::class.java)
    }
}

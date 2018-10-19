package com.sven.web.configuration.intercepter

import com.sven.web.util.logger.HttpLogger
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

/**
 * 统一请求日志输出
 */
@Aspect
@Order(1)
@Component
class LoggerInterceptor {
    @Around("execution (* com.sven.web.controller.**.*(..))")
    fun log(joinPoint: ProceedingJoinPoint): Any? {
        val requestAttributes = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        val request = requestAttributes.request
        val response = requestAttributes.response!!
        // 只会打印正常请求的log, 404等错误无法打印
        HttpLogger.logRequestIncoming(request)
        try {
            return joinPoint.proceed(joinPoint.args)
        } finally {
            HttpLogger.logResponseOuting(request, response)
        }
    }
}
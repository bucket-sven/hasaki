package com.sven.web.configuration.intercepter

import com.sven.web.util.logger.HttpLogger
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

/**
 * 统一请求日志输出
 */
@Aspect
@Order(1)
@Component
class LoggerInterceptor {
    /**
     * 打印正常http请求
     */
    @Around("execution(* com.sven.web.controller.**.*(..))")
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

    /**
     * 打印异常http请求, 如404
     */
    @Around("execution(* com.sven.web.controller.advice.**.*(..))")
    fun logAdvice(joinPoint: ProceedingJoinPoint): Any? {
        val requestAttributes = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        val request = requestAttributes.request
        val response = requestAttributes.response!!
        HttpLogger.logRequestIncoming(request)
        val methodSignature = joinPoint.signature as MethodSignature
        val responseStatus = methodSignature.method.getAnnotation(ResponseStatus::class.java)
        var status = 500
        if (responseStatus != null) {
            status = responseStatus.value.value()
        }
        try {
            return joinPoint.proceed(joinPoint.args)
        } finally {
            HttpLogger.logResponseOuting(request, response, status)
        }
    }
}
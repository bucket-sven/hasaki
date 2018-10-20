package com.sven.web.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.sven.web.configuration.multicontenttype.CustomRequestParamsArgumentResolver
import com.sven.web.util.Constants
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.text.SimpleDateFormat

@Configuration
class CustomMvcConfigurationAdapter : WebMvcConfigurer {
    /**
     * 使用自定义的request请求参数解析器，使之同时能解析application/json，application/x-www-urlencoded-form格式的数据
     */
    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        argumentResolvers.add(CustomRequestParamsArgumentResolver())
    }

    /**
     * api返回的JSON数据中，使用的日期格式
     */
    @Bean
    fun getObjectMapper(): ObjectMapper {
        val obj = ObjectMapper()
        obj.dateFormat = SimpleDateFormat(Constants.DATE_FORMAT)
        return obj
    }

}
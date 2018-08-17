package com.sven.web.configuration

import com.baomidou.mybatisplus.plugins.PerformanceInterceptor
import com.fasterxml.jackson.databind.ObjectMapper
import com.sven.web.configuration.multicontenttype.CustomRequestParamsArgumentResolver
import com.sven.web.util.Constants
import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.text.SimpleDateFormat

@Configuration
class CustomMvcConfigurationAdapter : WebMvcConfigurer {
    override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
//        converters.add(messageConverter())
    }

    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        argumentResolvers.add(CustomRequestParamsArgumentResolver())
    }

    @Bean
    @Profile("dev", "test")
    fun performanceInterceptor(): PerformanceInterceptor {
        return PerformanceInterceptor()
    }

    @Bean
    fun getObjectMapper(): ObjectMapper {
        val obj = ObjectMapper()
        obj.dateFormat = SimpleDateFormat(Constants.DATE_FORMAT)
        return obj
    }

//    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
//        super.configureMessageConverters(converters)
//    }

//    @Bean
//    fun messageConverter(): CustomHttpMessageConverter {
//        return CustomHttpMessageConverter()
//    }
}
package com.sven.web.configuration.multicontenttype

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.sven.web.common.error.framework.ParamsError
import com.sven.web.util.validator.ValidationUtil
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import java.io.IOException
import javax.servlet.http.HttpServletRequest

/**
 * 此类与GlobalExceptionHandler有诸多联系，比如异常拋出后，由GlobalExceptionHandler处理, 请求日志打印也需要与之对应
 */
//@EnableWebMvc
class CustomRequestParamsArgumentResolver : HandlerMethodArgumentResolver {
    private val attrName = "JSON_REQUEST_BODY"
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(CustomRequestParams::class.java)
    }

    /**
     * 合并application/json的body 和 x-www-urlencoded-form-urlencoded的body,以及query参数
     * 此方法只有在注入CustomRequestParams注解上的参数有效
     */
    override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?, webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): Any {
        val type = parameter.genericParameterType
        val servletRequest = webRequest.getNativeRequest(HttpServletRequest::class.java)!!
        val json = getRequestBody(webRequest, servletRequest)
        val query = servletRequest.parameterMap
        var map = JSONObject()
        if (json != null) {
            map = JSON.parseObject(json)
        }
        query.forEach { t, u ->
            map[t] = u[0]
        }
        val str = map.toJSONString()
        var ret = JSON.parseObject<Any>(str, type)
        // 如果传入参数无法构造CustomRequestParams修饰的对象
        if (ret == null) {
            // 获取参数最少的构造函数，构造一个默认对象进行参数验证
            val constructors = Class.forName(type.typeName).declaredConstructors
            constructors.sortBy { it.parameterCount }
            val constructor = constructors[0]
            val args = arrayListOf<Any?>()
            constructor.parameters.forEach {
                args.add(null)
            }
            ret = constructor.newInstance(*args.toArray())
        }
        val valid = ValidationUtil.validateEntity(ret)
        if (valid.hasErrors) {
            val errors = valid.errorMsg?.map { it.key + it.value }?.joinToString(",")
            throw ParamsError("params_error", errors!!)
        }
        return ret
    }

    private fun shouldParseJSON(webRequest: HttpServletRequest): Boolean {
        val contentTypes = arrayOf(MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE)
        return contentTypes.contains(webRequest.contentType)
    }

    private fun getRequestBody(webRequest: NativeWebRequest, servletRequest: HttpServletRequest): String? {
        var jsonBody = webRequest.getAttribute(attrName, NativeWebRequest.SCOPE_REQUEST) as String?
        if (!shouldParseJSON(servletRequest)) {
            return null
        }
        if (jsonBody == null) {
            try {
                val inputStream = servletRequest.reader
                val bytes = CharArray(1024)
                val outString = StringBuilder()
                while (true) {
                    val count = inputStream.read(bytes, 0, bytes.size)
                    if (count < 0)
                        break
                    outString.append(bytes, 0, count)
                }
                jsonBody = outString.toString()
                webRequest.setAttribute(attrName, jsonBody, NativeWebRequest.SCOPE_REQUEST)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return jsonBody
    }
}
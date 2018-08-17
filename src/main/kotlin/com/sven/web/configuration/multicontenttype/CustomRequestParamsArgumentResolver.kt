package com.sven.web.configuration.multicontenttype

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.sven.web.util.validator.ValidationUtil
import com.sven.web.util.error.ParamsError
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import java.io.IOException
import javax.servlet.http.HttpServletRequest

class CustomRequestParamsArgumentResolver : HandlerMethodArgumentResolver {
    private val attrName = "JSON_REQUEST_BODY"
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(CustomRequestParams::class.java)
    }

    // 合并application/json的body 和 x-www-urlencoded-form-urlencoded的body,以及query参数
    // 此方法只有在注入CustomRequestParams注解上的参数有效
    override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?, webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): Any? {
        val type = parameter.genericParameterType
        val json = getRequestBody(webRequest)
        val query = queryMap(webRequest)
        var map = JSONObject()
        if (json != null) {
            map = JSON.parseObject(json)
        }
        query.forEach { t, u ->
            map[t] = u[0]
        }
        val str = JSON.toJSONString(map)
        val ret = JSON.parseObject<Any>(str, type)
        val valid = ValidationUtil.validateEntity(ret)
        if (valid.hasErrors) {
            val errors = valid.errorMsg?.map { it.key + it.value }?.joinToString(",")
            throw ParamsError("params_error", errors!!)
        }
        return ret
    }

    private fun queryMap(webRequest: NativeWebRequest): Map<String, Array<out String>> {
        val servletRequest = webRequest.getNativeRequest(HttpServletRequest::class.java)!!
        return servletRequest.parameterMap
    }

    private fun getRequestBody(webRequest: NativeWebRequest): String? {
        val servletRequest = webRequest.getNativeRequest(HttpServletRequest::class.java)!!
        var jsonBody = webRequest.getAttribute(attrName, NativeWebRequest.SCOPE_REQUEST) as String?
        val contentTypes = arrayOf(MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE)
        if (!contentTypes.contains(servletRequest.contentType)) {
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
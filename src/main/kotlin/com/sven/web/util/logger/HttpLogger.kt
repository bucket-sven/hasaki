package com.sven.web.util.logger

import org.slf4j.LoggerFactory
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * HTTP请求日志输出格式
 */
object HttpLogger {
    private val logger = LoggerFactory.getLogger(HttpLogger::class.java)
    private const val requestStartTimeKey = "REQUEST_LOG_START_TIME"
    fun logRequestIncoming(request: HttpServletRequest) {
        val now = System.currentTimeMillis()
        request.setAttribute(requestStartTimeKey, now)
        val path = getFullQueryPath(request)

        logger.info("<- {} {}", request.method, path)
    }

    fun logResponseOuting(request: HttpServletRequest, response: HttpServletResponse, status: Int? = null) {
        val path = getFullQueryPath(request)
        var statusCode = status
        val startTime = request.getAttribute(requestStartTimeKey) as Long?
        var ms: Long? = null
        if (startTime != null) {
            ms = System.currentTimeMillis() - startTime
        }
        if (statusCode == null) {
            statusCode = response.status
        }
        logger.info("-> {} {} {} {}ms", request.method, path, statusCode, ms)
    }

    /**
     * 获取请求uri路径， /path?query
     * @param request
     * @return uri
     */
    private fun getFullQueryPath(request: HttpServletRequest): String {
        var path = request.servletPath
        if (!request.queryString.isNullOrEmpty()) {
            path += "?${request.queryString}"
        }
        return path
    }
}
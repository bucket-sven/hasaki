package com.sven.web

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@MapperScan(basePackages = ["com.sven.web.dao.mapper"])
class HasakiApplication

fun main(args: Array<String>) {
    runApplication<HasakiApplication>(*args)
}

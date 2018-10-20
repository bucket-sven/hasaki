package com.sven.web

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class HasakiApplication

fun main(args: Array<String>) {
    runApplication<HasakiApplication>(*args)
}

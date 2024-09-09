package com.ephirium.coffee_backend

import com.ephirium.coffee_backend.core.config.initConfigs
import com.ephirium.coffee_backend.plugins.*
import io.ktor.server.application.Application

fun main(args: Array<String>) {
    io.ktor.server.cio.EngineMain.main(args)
}

@Suppress("unused")
fun Application.module() {
    initConfigs()

    configureKoin()
    configureSerialization()
    configureMonitoring()
    configureHeaders()
    configureSecurity()
    configureRouting()
}

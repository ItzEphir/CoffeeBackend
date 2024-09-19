package com.ephirium.coffee_backend.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.defaultheaders.*

fun Application.configureHeaders() {
    install(DefaultHeaders) {
        header(name = "X-Engine", value = "Ktor") // will send this header with each response
        header(name = HttpHeaders.Server, "Coffee-Server")
    }
}

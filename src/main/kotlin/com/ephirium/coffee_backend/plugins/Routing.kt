package com.ephirium.coffee_backend.plugins

import com.ephirium.coffee_backend.feature.auth.controller.AuthController
import com.ephirium.coffee_backend.feature.auth.routes.authRoute
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val authController: AuthController by inject()

    routing {
        authRoute(authController)
    }
}

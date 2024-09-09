package com.ephirium.coffee_backend.feature.auth.routes

import com.ephirium.coffee_backend.feature.auth.controller.AuthController
import com.ephirium.coffee_backend.feature.auth.model.requests.SignInRequest
import com.ephirium.coffee_backend.feature.auth.model.requests.SignUpRequest
import com.ephirium.coffee_backend.feature.auth.model.responses.SignInResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.authRoute(authController: AuthController) =
    route("/auth") {
        signIn(authController)
        signUp(authController)
    }

private fun Route.signIn(authController: AuthController) = post("/sign-in") {
    val signInRequest = runCatching { call.receive<SignInRequest>() }
        .getOrElse { return@post call.respond(HttpStatusCode.BadRequest, "Invalid request body") }
    val user = authController.signIn(signInRequest)
        ?: return@post call.respond(HttpStatusCode.NotFound, "User not found")

    val (access, refresh) = authController.generateTokens(user)
    call.respond(HttpStatusCode.OK, SignInResponse(access, refresh))
}

private fun Route.signUp(authController: AuthController) = post("/sign-up") {
    val signUpRequest = runCatching { call.receive<SignUpRequest>() }
        .getOrElse { return@post call.respond(HttpStatusCode.BadRequest, "Invalid request body") }
    val user = authController.signUp(signUpRequest)
        ?: return@post call.respond(HttpStatusCode.BadRequest, "User not found")

    val (access, refresh) = authController.generateTokens(user)
    call.respond(HttpStatusCode.OK, SignInResponse(access, refresh))
}
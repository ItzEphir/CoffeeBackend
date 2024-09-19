package com.ephirium.coffee_backend.feature.auth.routes

import com.auth0.jwt.JWT
import com.ephirium.coffee_backend.feature.auth.controller.AuthController
import com.ephirium.coffee_backend.feature.auth.controller.AuthExceptions
import com.ephirium.coffee_backend.feature.auth.model.requests.SignInRequest
import com.ephirium.coffee_backend.feature.auth.model.requests.SignUpRequest
import com.ephirium.coffee_backend.feature.auth.model.responses.AuthenticateResponse
import com.ephirium.coffee_backend.feature.auth.model.responses.RefreshResponse
import com.ephirium.coffee_backend.feature.auth.model.responses.SignInResponse
import com.ephirium.coffee_backend.feature.auth.model.responses.SignUpResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*

fun Routing.authRoute(authController: AuthController) =
    route("/auth") {
        signIn(authController)
        signUp(authController)
        authenticate(authController)
        refresh(authController)
    }

private fun Throwable.getAuthThrowableToCall(): suspend PipelineContext<Unit, ApplicationCall>.() -> Unit {
    if (this is AuthExceptions) run {
        when (this) {
            is AuthExceptions.InternalError -> return {
                call.respond(HttpStatusCode.InternalServerError, this@getAuthThrowableToCall.message)
            }

            is AuthExceptions.HashingValidation -> return {
                call.respond(HttpStatusCode.Unauthorized, this@getAuthThrowableToCall.message)
            }

            is AuthExceptions.PasswordCannotBeCreated -> return {
                call.respond(HttpStatusCode.Conflict, this@getAuthThrowableToCall.message)
            }

            is AuthExceptions.PayloadNotFound -> return {
                call.respond(HttpStatusCode.NotFound, this@getAuthThrowableToCall.message)
            }

            is AuthExceptions.UserCannotBeCreated -> return {
                call.respond(HttpStatusCode.Conflict, this@getAuthThrowableToCall.message)
            }

            is AuthExceptions.UserNotFound -> return {
                call.respond(HttpStatusCode.NotFound, this@getAuthThrowableToCall.message)
            }
        }
    } else {
        printStackTrace()
        return {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}

private fun Route.signIn(authController: AuthController) = post("/sign-in") {
    val signInRequest = runCatching { call.receive<SignInRequest>() }
        .getOrElse { return@post call.respond(HttpStatusCode.BadRequest, "Invalid request body") }
    val user = authController.signIn(signInRequest).getOrElse {
        return@post it.getAuthThrowableToCall()()
    }

    val (access, refresh) = authController.generateTokens(user)
    call.respond(HttpStatusCode.OK, SignInResponse(access, refresh))
}

private fun Route.signUp(authController: AuthController) = post("/sign-up") {
    val signUpRequest = runCatching { call.receive<SignUpRequest>() }
        .getOrElse { return@post call.respond(HttpStatusCode.BadRequest, "Invalid request body") }
    val user = authController.signUp(signUpRequest).getOrElse {
        return@post it.getAuthThrowableToCall()()
    }

    val (access, refresh) = authController.generateTokens(user)
    call.respond(HttpStatusCode.OK, SignUpResponse(access, refresh))
}

private fun Route.authenticate(authController: AuthController) = authenticate {
    get("/") {
        val payload = call.principal<JWTPrincipal>()?.payload ?: return@get call.respond(HttpStatusCode.Unauthorized)
        val user = authController.authenticate(payload).getOrElse {
            return@get it.getAuthThrowableToCall()()
        }
        val (access, refresh) = authController.generateTokens(user)
        val userBody = AuthenticateResponse.User(user.id, user.login, user.name)
        call.respond(HttpStatusCode.OK, AuthenticateResponse(userBody, access, refresh))
    }
}

private fun Route.refresh(authController: AuthController) = get("/refresh") {
    val token = (
            call.request.queryParameters["refresh_token"]
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid refresh token")
            ).run { JWT.decode(this) }

    val user = authController.refresh(token).getOrElse {
        return@get it.getAuthThrowableToCall()()
    }

    val (access, refresh) = authController.generateTokens(user)
    call.respond(HttpStatusCode.OK, RefreshResponse(access, refresh))
}
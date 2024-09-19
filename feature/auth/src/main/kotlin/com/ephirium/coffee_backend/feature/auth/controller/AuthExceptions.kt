package com.ephirium.coffee_backend.feature.auth.controller

sealed class AuthExceptions(override val message: String): Exception(message) {
    class UserCannotBeCreated: AuthExceptions("User cannot be created")
    class PasswordCannotBeCreated: AuthExceptions("Password cannot be created")
    class UserNotFound: AuthExceptions("User not found")
    class HashingValidation: AuthExceptions("Hashing was not verified")
    class PayloadNotFound: AuthExceptions("Payload not found")

    class InternalError: AuthExceptions("Internal error")
}
package com.ephirium.coffee_backend.feature.auth.model.requests

import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(
    val login: String,
    val password: String,
)

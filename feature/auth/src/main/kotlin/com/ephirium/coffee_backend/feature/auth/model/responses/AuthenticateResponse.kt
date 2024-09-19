package com.ephirium.coffee_backend.feature.auth.model.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticateResponse(
    val user: User,
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String,
){
    @Serializable
    data class User(
        val id: String,
        val login: String,
        val name: String,
    )
}

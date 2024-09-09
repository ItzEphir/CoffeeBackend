package com.ephirium.coffee_backend.core.config.token

import kotlin.time.Duration

data class TokenConfig(
    val issuer: String,
    val audience: String,
    val secret: String,
    val expirationTimes: TokenExpirationTimes,
) {
    data class TokenExpirationTimes(
        val accessToken: Duration,
        val refreshToken: Duration,
    )
}

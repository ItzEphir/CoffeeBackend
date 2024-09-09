package com.ephirium.coffee_backend.core.token.service

import alsolog
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.ephirium.coffee_backend.core.config.token.TokenConfig
import com.ephirium.coffee_backend.core.token.model.TokenClaim
import com.ephirium.coffee_backend.core.token.service.TokenService.TokenType
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant

internal class JWTTokenService(private val tokenConfig: TokenConfig) : TokenService {
    override fun generate(tokenType: TokenType, vararg claims: TokenClaim): String =
        JWT.create()
            .withAudience(tokenConfig.audience)
            .withIssuer(tokenConfig.issuer)
            .withExpiresAt(
                (Clock.System.now() + when (tokenType) {
                    TokenType.ACCESS -> tokenConfig.expirationTimes.accessToken
                    TokenType.REFRESH -> tokenConfig.expirationTimes.refreshToken
                }).toJavaInstant()
            )
            .run { claims.fold(initial = this) { acc, it -> acc.withClaim(it.name, it.value) } }
            .sign(Algorithm.HMAC256(tokenConfig.secret))
            .alsolog("Generated:")
}
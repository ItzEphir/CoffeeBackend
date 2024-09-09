package com.ephirium.coffee_backend.core.token.service

import alsolog
import com.ephirium.coffee_backend.core.token.model.TokenClaim
import com.ephirium.coffee_backend.core.token.model.Tokens

fun TokenService.generateAccessToken(vararg claims: TokenClaim) =
    generate(TokenService.TokenType.ACCESS, *claims).alsolog("access token")

fun TokenService.generateRefreshToken(vararg claims: TokenClaim) =
    generate(TokenService.TokenType.REFRESH, *claims).alsolog("refresh token")

fun TokenService.generate(vararg claims: TokenClaim) =
    Tokens(generateAccessToken(*claims), generateRefreshToken(*claims)).alsolog("tokens")
package com.ephirium.coffee_backend.core.token.service

import com.ephirium.coffee_backend.core.token.model.TokenClaim

interface TokenService {
    fun generate(tokenType: TokenType, vararg claims: TokenClaim): String

    fun decode(token: String): List<TokenClaim>

    enum class TokenType { ACCESS, REFRESH }
}
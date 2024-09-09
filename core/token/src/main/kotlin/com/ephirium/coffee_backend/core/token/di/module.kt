package com.ephirium.coffee_backend.core.token.di

import com.ephirium.coffee_backend.core.config.token.tokenConfig
import com.ephirium.coffee_backend.core.token.service.JWTTokenService
import com.ephirium.coffee_backend.core.token.service.TokenService
import org.koin.dsl.bind
import org.koin.dsl.module

val tokenModule = module {
    single{ JWTTokenService(tokenConfig) } bind TokenService::class
}
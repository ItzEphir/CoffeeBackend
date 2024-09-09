package com.ephirium.coffee_backend.core.config

import com.ephirium.coffee_backend.core.config.token.initTokenConfig
import io.ktor.server.application.*

fun Application.initConfigs() {
    initTokenConfig()
}
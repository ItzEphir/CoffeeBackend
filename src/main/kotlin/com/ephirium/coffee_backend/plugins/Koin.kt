package com.ephirium.coffee_backend.plugins

import com.ephirium.coffee_backend.di.mainModule
import com.ephirium.coffee_backend.feature.auth.di.authFeatureModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        modules(mainModule, authFeatureModule)
    }
}

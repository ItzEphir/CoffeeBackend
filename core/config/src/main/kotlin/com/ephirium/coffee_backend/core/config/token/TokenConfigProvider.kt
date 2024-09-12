package com.ephirium.coffee_backend.core.config.token

import com.ephirium.coffee_backend.core.config.ConfigAlreadyDefinedException
import com.ephirium.coffee_backend.core.config.UndefinedApplicationConfigException
import com.ephirium.coffee_backend.core.config.token.JwtFields.ACCESS_EXPIRATION
import com.ephirium.coffee_backend.core.config.token.JwtFields.AUDIENCE
import com.ephirium.coffee_backend.core.config.token.JwtFields.DOMAIN
import com.ephirium.coffee_backend.core.config.token.JwtFields.REALM
import com.ephirium.coffee_backend.core.config.token.JwtFields.REFRESH_EXPIRATION
import com.ephirium.coffee_backend.core.config.token.JwtFields.SECRET
import io.ktor.server.application.*
import io.ktor.server.config.*
import kotlin.time.Duration.Companion.days

private var config: TokenConfig? = null
val tokenConfig by lazy { config ?: throw UndefinedTokenConfigException() }

private object JwtFields {
    const val DOMAIN = "jwt.domain"
    const val AUDIENCE = "jwt.audience"
    const val SECRET = "jwt.secret"
    const val REALM = "jwt.realm"
    const val ACCESS_EXPIRATION = "jwt.expirationTimesDays.access"
    const val REFRESH_EXPIRATION = "jwt.expirationTimesDays.refresh"
}

fun ApplicationEnvironment.initTokenConfig() = config.initTokenConfig()

fun Application.initTokenConfig() = environment.initTokenConfig()

fun ApplicationConfig.initTokenConfig() {
    if (config != null) throw ConfigAlreadyDefinedException()
    config = TokenConfig(
        issuer = tryGetString(DOMAIN) ?: throw UndefinedApplicationConfigException(DOMAIN),
        audience = tryGetString(AUDIENCE) ?: throw UndefinedApplicationConfigException(AUDIENCE),
        secret = tryGetString(SECRET) ?: throw UndefinedApplicationConfigException(SECRET),
        realm = tryGetString(REALM) ?: throw UndefinedApplicationConfigException(REALM),
        expirationTimes = TokenConfig.TokenExpirationTimes(
            accessToken = runCatching { property(ACCESS_EXPIRATION).getInt().days }
                .getOrElse { throw UndefinedApplicationConfigException(ACCESS_EXPIRATION) },
            refreshToken = runCatching { property(REFRESH_EXPIRATION).getInt().days }
                .getOrElse { throw UndefinedApplicationConfigException(REFRESH_EXPIRATION) },
        ),
    )
}

private fun ApplicationConfigValue.getInt() = getString().toInt()
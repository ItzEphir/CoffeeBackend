package com.ephirium.coffee_backend.feature.auth.controller

import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.Payload
import com.ephirium.coffee_backend.core.hashing.service.HashingService
import com.ephirium.coffee_backend.core.token.model.TokenClaim
import com.ephirium.coffee_backend.core.token.model.Tokens
import com.ephirium.coffee_backend.core.token.service.TokenService
import com.ephirium.coffee_backend.core.token.service.generate
import com.ephirium.coffee_backend.data.password.model.entity.Password
import com.ephirium.coffee_backend.data.password.service.PasswordService
import com.ephirium.coffee_backend.data.password.service.findPasswordSaltedHashByLogin
import com.ephirium.coffee_backend.data.user.model.entity.User
import com.ephirium.coffee_backend.data.user.service.UserService
import com.ephirium.coffee_backend.feature.auth.model.requests.SignInRequest
import com.ephirium.coffee_backend.feature.auth.model.requests.SignUpRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.toKotlinInstant
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class AuthController(
    private val userService: UserService,
    private val passwordService: PasswordService,
    private val hashingService: HashingService,
    private val tokenService: TokenService,
) {
    suspend fun signIn(request: SignInRequest): Result<User> = withContext(Dispatchers.IO) {
        val (login, password) = request
        val passwordSaltedHash = passwordService
            .findPasswordSaltedHashByLogin(login)
            ?: return@withContext Result.failure(AuthExceptions.UserNotFound())
        actionToResult<Unit, AuthExceptions.HashingValidation>(exceptionClass = AuthExceptions.HashingValidation::class) {
            if (hashingService.verify(password, passwordSaltedHash)) Unit else null
        }.map {
            userService.findByLogin(login) ?: return@withContext Result.failure(AuthExceptions.UserNotFound())
        }
    }

    suspend fun signUp(request: SignUpRequest): Result<User> = withContext(Dispatchers.IO) {
        val (login, password, name) = request
        var user = User(login = login, name = name)
        user = user.copy(
            id = userService.create(user) ?: return@withContext Result.failure(AuthExceptions.UserCannotBeCreated())
        )
        val passwordSaltedHash = hashingService.generateSaltedHash(password)
        actionToResult(exceptionClass = AuthExceptions.PasswordCannotBeCreated::class) {
            passwordService.create(Password(login = login, passwordSaltedHash = passwordSaltedHash))
        }.onFailure {
            userService.delete(user.id) ?: return@withContext Result.failure(AuthExceptions.InternalError())
        }.map {
            user
        }
    }

    suspend fun authenticate(payload: Payload): Result<User> = withContext(Dispatchers.IO) {
        val login = payload.claims[User::login.name]?.asString()
            ?: return@withContext Result.failure(AuthExceptions.PayloadNotFound())
        actionToResult(exceptionClass = AuthExceptions.UserNotFound::class) {
            userService.findByLogin(login)
        }
    }

    suspend fun refresh(payload: Payload): Result<User> = withContext(Dispatchers.IO) {
        if (payload.expiresAtAsInstant.toKotlinInstant() <= Clock.System.now()) {
            return@withContext Result.failure(
                TokenExpiredException("Refresh token expired", payload.expiresAtAsInstant)
            )
        }
        val login = payload.claims[User::login.name]?.asString()
            ?: return@withContext Result.failure(AuthExceptions.PayloadNotFound())
        actionToResult(exceptionClass = AuthExceptions.UserNotFound::class) {
            userService.findByLogin(login)
        }
    }

    fun generateTokens(user: User): Tokens =
        tokenService.generate(
            TokenClaim(User::id.name, user.id),
            TokenClaim(User::name.name, user.name),
            TokenClaim(User::login.name, user.login),
        )

    private inline fun <reified T, reified R : AuthExceptions> actionToResult(
        exceptionClass: KClass<R>,
        action: () -> T?,
    ): Result<T> = when (val res = action()) {
        null -> Result.failure(exceptionClass.createInstance())
        else -> Result.success(res)
    }
}
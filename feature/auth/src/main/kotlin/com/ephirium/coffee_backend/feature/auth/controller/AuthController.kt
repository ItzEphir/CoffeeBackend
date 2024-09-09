package com.ephirium.coffee_backend.feature.auth.controller

import alsolog
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

class AuthController(
    private val userService: UserService,
    private val passwordService: PasswordService,
    private val hashingService: HashingService,
    private val tokenService: TokenService,
) {
    suspend fun signIn(request: SignInRequest): User? {
        val (login, password) = request
        val passwordSaltedHash = passwordService
            .findPasswordSaltedHashByLogin(login)
            ?: return null
        return if (hashingService.verify(password, passwordSaltedHash))
            userService.findByLogin(login)
        else null
    }

    suspend fun signUp(request: SignUpRequest): User? {
        val (login, password, name) = request
        var user = User(login = login, name = name)
        user = user.copy(id = userService.create(user) ?: return null)
        val passwordSaltedHash = hashingService.generateSaltedHash(password)
        return if (passwordService.create(
                Password(login = login, passwordSaltedHash = passwordSaltedHash)
            ) == null
        ) run {
            userService.delete(user.id) ?: throw Exception()
            null
        } else user

    }

    fun generateTokens(user: User): Tokens =
        tokenService.generate(
            TokenClaim(User::id.name, user.id),
            TokenClaim(User::name.name, user.name),
            TokenClaim(User::login.name, user.login),
        )

}
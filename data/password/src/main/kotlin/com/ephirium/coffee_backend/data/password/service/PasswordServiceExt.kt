package com.ephirium.coffee_backend.data.password.service

suspend fun PasswordService.findPasswordSaltedHashByLogin(login: String) =
    findByLogin(login)?.passwordSaltedHash
package com.ephirium.coffee_backend.data.password.service

import com.ephirium.coffee_backend.data.password.model.entity.Password

interface PasswordService {
    suspend fun create(password: Password): String? // id
    suspend fun update(password: Password): Password? // replaced password

    suspend fun findByLogin(login: String): Password? // found password
    suspend fun deleteByLogin(login: String): String? // id
}
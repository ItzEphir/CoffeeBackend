package com.ephirium.coffee_backend.data.password.service

import com.ephirium.coffee_backend.data.password.model.entity.Password

interface PasswordService {
    suspend fun create(password: Password): CreatedId?
    suspend fun read(id: String): FoundPassword?
    suspend fun update(password: Password): ReplacedPassword?
    suspend fun delete(id: String): DeletedId?

    suspend fun findByLogin(login: String): FoundPassword?
    suspend fun deleteByLogin(login: String): DeletedId?
}
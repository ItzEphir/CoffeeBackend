package com.ephirium.coffee_backend.data.user.service

import com.ephirium.coffee_backend.data.user.model.entity.User

interface UserService {
    suspend fun create(user: User): String? // id
    suspend fun read(id: String): User? // found user
    suspend fun update(user: User): User? // replaced user
    suspend fun delete(id: String): String? // login

    suspend fun findByLogin(login: String): User? // found user
    suspend fun deleteByLogin(login: String): String? // id
}
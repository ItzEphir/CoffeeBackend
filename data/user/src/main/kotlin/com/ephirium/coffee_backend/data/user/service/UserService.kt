package com.ephirium.coffee_backend.data.user.service

import com.ephirium.coffee_backend.data.user.model.entity.User

interface UserService {
    suspend fun create(user: User): CreatedId?
    suspend fun read(id: String): FoundUserDto?
    suspend fun update(user: User): ReplacedUserDto?
    suspend fun delete(id: String): DeletedLogin?

    suspend fun findByLogin(login: String): FoundUserDto?
    suspend fun deleteByLogin(login: String): DeletedLogin?
}
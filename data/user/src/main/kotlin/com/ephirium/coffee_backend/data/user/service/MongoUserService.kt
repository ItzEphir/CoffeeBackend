package com.ephirium.coffee_backend.data.user.service

import alsolog
import com.ephirium.coffee_backend.data.user.mapper.toDto
import com.ephirium.coffee_backend.data.user.mapper.toUser
import com.ephirium.coffee_backend.data.user.model.dto.UserDto
import com.ephirium.coffee_backend.data.user.model.entity.User
import com.mongodb.client.model.Filters
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import org.bson.types.ObjectId

internal class MongoUserService(database: MongoDatabase) : UserService {

    private val collection = database.getCollection<UserDto>(COLLECTION)

    override suspend fun create(user: User): CreatedId? = withContext(Dispatchers.IO) {
        runCatching { collection.createIndex(Indexes.text(UserDto::login::name.name), IndexOptions().unique(true)) }
            .getOrElse { return@withContext null }
            .alsolog("Created index:")

        collection.insertOne(user.toDto()).insertedId?.asObjectId()?.value?.toHexString()
            ?.alsolog("Created id:")
    }

    override suspend fun read(id: String): FoundUserDto? = withContext(Dispatchers.IO) {
        collection.find(Filters.eq(ID, ObjectId(id))).firstOrNull()?.toUser()
            ?.alsolog("Read:")
    }

    override suspend fun update(user: User): ReplacedUserDto? = withContext(Dispatchers.IO) {
        val userDto = user.toDto()
        collection.findOneAndReplace(Filters.eq(ID, userDto.id), userDto)?.toUser()
            ?.alsolog("Updated:")
    }

    override suspend fun delete(id: String): DeletedLogin? = withContext(Dispatchers.IO) {
        collection.findOneAndDelete(Filters.eq(ID, ObjectId(id)))?.login
            ?.alsolog("Deleted login:")
    }

    override suspend fun findByLogin(login: String): FoundUserDto? = withContext(Dispatchers.IO) {
        collection.find(Filters.eq(UserDto::login.name, login)).firstOrNull()?.toUser()
            ?.alsolog("Read:")
    }

    override suspend fun deleteByLogin(login: String): DeletedLogin? = withContext(Dispatchers.IO) {
        collection.findOneAndDelete(Filters.eq(UserDto::login.name))?.login
            ?.alsolog("Deleted login:")
    }

    private companion object {
        private const val COLLECTION = "users"
        private const val ID = "_id"
    }
}
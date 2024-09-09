package com.ephirium.coffee_backend.data.password.service

import alsolog
import com.ephirium.coffee_backend.data.password.mapper.toDto
import com.ephirium.coffee_backend.data.password.mapper.toPassword
import com.ephirium.coffee_backend.data.password.model.dto.PasswordDto
import com.ephirium.coffee_backend.data.password.model.entity.Password
import com.mongodb.client.model.Filters
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import org.bson.types.ObjectId

class MongoPasswordService(database: MongoDatabase) : PasswordService {

    private val collection = database.getCollection<PasswordDto>(COLLECTION)

    override suspend fun create(password: Password): CreatedId? = withContext(Dispatchers.IO) {
        runCatching { collection.createIndex(Indexes.text(PasswordDto::login.name), IndexOptions().unique(true)) }
            .getOrElse { return@withContext null }
            .alsolog("Created index:")

        collection.insertOne(password.toDto()).insertedId?.asObjectId()?.value?.toHexString()
            ?.alsolog("Created id:")
    }

    override suspend fun read(id: String): FoundPassword? = withContext(Dispatchers.IO) {
        collection.find(Filters.eq(ID, ObjectId(id))).firstOrNull()?.toPassword()
            ?.alsolog("Read:")
    }

    override suspend fun update(password: Password): ReplacedPassword? = withContext(Dispatchers.IO) {
        val passwordDto = password.toDto()
        collection.findOneAndReplace(Filters.eq(ID, passwordDto.id), passwordDto)?.toPassword()
            ?.alsolog("Updated:")
    }

    override suspend fun delete(id: String): DeletedId? = withContext(Dispatchers.IO) {
        collection.findOneAndDelete(Filters.eq(ID, ObjectId(id)))?.id?.toHexString()
            ?.alsolog("Deleted id:")
    }

    override suspend fun findByLogin(login: String): FoundPassword? =
        withContext(Dispatchers.IO) {
            collection.find(Filters.eq(PasswordDto::login.name, login)).firstOrNull()?.toPassword()
                ?.alsolog("Read:")
        }

    override suspend fun deleteByLogin(login: String): DeletedId? =
        withContext(Dispatchers.IO) {
            collection.findOneAndDelete(Filters.eq(PasswordDto::login.name, login))?.id?.toHexString()
                ?.alsolog("Deleted id:")
        }

    private companion object {
        private const val COLLECTION = "passwords"
        private const val ID = "_id"
    }
}
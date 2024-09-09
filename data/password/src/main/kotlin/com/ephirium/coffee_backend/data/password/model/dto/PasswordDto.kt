package com.ephirium.coffee_backend.data.password.model.dto

import com.ephirium.coffee_backend.core.hashing.model.SaltedHash
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

internal data class PasswordDto(
    @BsonId
    val id: ObjectId = ObjectId(),
    val login: String,
    val passwordSaltedHash: SaltedHash,
)

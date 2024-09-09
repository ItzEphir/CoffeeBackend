package com.ephirium.coffee_backend.data.user.model.dto

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

internal data class UserDto(
    @BsonId
    val id: ObjectId = ObjectId(),
    val login: String,
    val name: String,
)

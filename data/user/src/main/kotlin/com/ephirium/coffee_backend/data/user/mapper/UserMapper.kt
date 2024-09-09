package com.ephirium.coffee_backend.data.user.mapper

import com.ephirium.coffee_backend.data.user.model.dto.UserDto
import com.ephirium.coffee_backend.data.user.model.entity.User
import org.bson.types.ObjectId

internal fun UserDto.toUser() = User(
    id = id.toHexString(),
    login = login,
    name = name,
)

internal fun User.toDto() = UserDto(
    id = if (id == "") ObjectId() else ObjectId(id),
    login = login,
    name = name,
)
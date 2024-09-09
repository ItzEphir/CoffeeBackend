package com.ephirium.coffee_backend.data.password.mapper

import com.ephirium.coffee_backend.data.password.model.dto.PasswordDto
import com.ephirium.coffee_backend.data.password.model.entity.Password
import org.bson.types.ObjectId

internal fun PasswordDto.toPassword() = Password(
    id = id.toHexString(),
    login = login,
    passwordSaltedHash = passwordSaltedHash,
)

internal fun Password.toDto() = PasswordDto(
    id = if(id == "") ObjectId() else ObjectId(id),
    login = login,
    passwordSaltedHash = passwordSaltedHash,
)
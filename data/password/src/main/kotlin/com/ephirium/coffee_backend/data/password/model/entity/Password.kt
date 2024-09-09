package com.ephirium.coffee_backend.data.password.model.entity

import com.ephirium.coffee_backend.core.hashing.model.SaltedHash

data class Password(
    val id: String = "",
    val login: String,
    val passwordSaltedHash: SaltedHash,
)

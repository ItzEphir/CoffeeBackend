package com.ephirium.coffee_backend.core.hashing.model

data class SaltedHash(
    val salt: String,
    val hash: String,
)

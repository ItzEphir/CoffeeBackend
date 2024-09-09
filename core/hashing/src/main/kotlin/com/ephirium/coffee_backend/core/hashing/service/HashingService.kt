package com.ephirium.coffee_backend.core.hashing.service

import com.ephirium.coffee_backend.core.hashing.model.SaltedHash

interface HashingService {
    fun generateSaltedHash(value: String, saltLength: Int = 32): SaltedHash
    fun verify(value: String, saltedHash: SaltedHash): Boolean
}
package com.ephirium.coffee_backend.core.hashing.service

import com.ephirium.coffee_backend.core.hashing.model.SaltedHash
import io.ktor.util.*
import org.apache.commons.codec.digest.DigestUtils
import java.security.SecureRandom

internal class Sha256HashingService : HashingService {

    override fun generateSaltedHash(value: String, saltLength: Int): SaltedHash {
        val salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLength).run { hex(this) }
        val hash = DigestUtils.sha256Hex("$salt$value")
        return SaltedHash(salt, hash)
    }

    override fun verify(value: String, saltedHash: SaltedHash): Boolean =
        DigestUtils.sha256Hex("${saltedHash.salt}$value") == saltedHash.hash
}
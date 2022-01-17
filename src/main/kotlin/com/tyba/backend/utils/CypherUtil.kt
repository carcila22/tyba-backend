package com.tyba.backend.utils

import org.apache.commons.codec.binary.Base64
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object CypherUtil {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    private const val PRIVATE_KEY = "//8\$pJePR<?\\\\x?pgT"

    fun encrypt(decrypted: String): String {

        var encryptedString  = ""

        try {

            val cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING")
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())

            val encrypted = cipher.doFinal(decrypted.toByteArray())

            encryptedString = Base64.encodeBase64String(encrypted)
        } catch (ex: Exception) {

            logger.error("encrypt error, message = {}", ex.message)
        }

        return encryptedString
    }

    private fun getSecretKey(): SecretKeySpec {

        var key = PRIVATE_KEY.toByteArray(charset(StandardCharsets.UTF_8.name()))
        val sha = MessageDigest.getInstance("SHA-1")

        key = sha.digest(key)
        key = key.copyOf(16)

        return SecretKeySpec(key, "AES")
    }
}
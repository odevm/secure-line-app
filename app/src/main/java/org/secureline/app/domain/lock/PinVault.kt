package org.secureline.app.domain.lock

import java.security.GeneralSecurityException
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

data class WrappedDbKey(
    val salt: ByteArray,
    val nonce: ByteArray,
    val ciphertext: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is WrappedDbKey) return false
        return salt.contentEquals(other.salt) &&
            nonce.contentEquals(other.nonce) &&
            ciphertext.contentEquals(other.ciphertext)
    }

    override fun hashCode(): Int = salt.contentHashCode()
}

/**
 * PIN-wraps the SecureDb AES key using PBKDF2-HMAC-SHA256 + AES-GCM.
 * Android Keystore biometric unlock can gate access to this blob later.
 */
object PinVault {
    private const val ITERATIONS = 120_000
    private const val KEY_BITS = 256
    private const val SALT_LEN = 16
    private const val NONCE_LEN = 12
    private const val MIN_PIN_LEN = 6

    fun generateDbKey(): ByteArray = ByteArray(32).also { SecureRandom().nextBytes(it) }

    fun wrap(
        pin: String,
        dbKey: ByteArray,
    ): WrappedDbKey {
        require(pin.length >= MIN_PIN_LEN) { "pin_too_short" }
        require(dbKey.size == 32) { "db_key_size" }
        val salt = ByteArray(SALT_LEN).also { SecureRandom().nextBytes(it) }
        val aes = derive(pin, salt)
        val nonce = ByteArray(NONCE_LEN).also { SecureRandom().nextBytes(it) }
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(aes, "AES"), GCMParameterSpec(128, nonce))
        return WrappedDbKey(salt = salt, nonce = nonce, ciphertext = cipher.doFinal(dbKey))
    }

    fun unwrap(
        pin: String,
        wrapped: WrappedDbKey,
    ): ByteArray {
        require(pin.length >= MIN_PIN_LEN) { "pin_too_short" }
        val aes = derive(pin, wrapped.salt)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        return try {
            cipher.init(
                Cipher.DECRYPT_MODE,
                SecretKeySpec(aes, "AES"),
                GCMParameterSpec(128, wrapped.nonce),
            )
            cipher.doFinal(wrapped.ciphertext)
        } catch (e: GeneralSecurityException) {
            throw GeneralSecurityException("wrong_pin", e)
        }
    }

    private fun derive(
        pin: String,
        salt: ByteArray,
    ): ByteArray {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(pin.toCharArray(), salt, ITERATIONS, KEY_BITS)
        return factory.generateSecret(spec).encoded
    }
}

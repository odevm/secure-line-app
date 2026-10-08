package org.secureline.app.domain.backup

import java.security.GeneralSecurityException
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

data class BackupBlob(
    val salt: ByteArray,
    val nonce: ByteArray,
    val ciphertext: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BackupBlob) return false
        return salt.contentEquals(other.salt) &&
            nonce.contentEquals(other.nonce) &&
            ciphertext.contentEquals(other.ciphertext)
    }

    override fun hashCode(): Int = salt.contentHashCode()
}

/**
 * Client-side Drive App Data backup envelope (passphrase ≠ app PIN).
 */
object BackupEnvelope {
    private const val MIN_PASSPHRASE = 8
    private const val ITERATIONS = 100_000

    fun encrypt(
        passphrase: String,
        plaintext: ByteArray,
    ): BackupBlob {
        require(passphrase.length >= MIN_PASSPHRASE) { "passphrase_too_short" }
        val salt = ByteArray(16).also { SecureRandom().nextBytes(it) }
        val key = derive(passphrase, salt)
        val nonce = ByteArray(12).also { SecureRandom().nextBytes(it) }
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, "AES"), GCMParameterSpec(128, nonce))
        return BackupBlob(salt, nonce, cipher.doFinal(plaintext))
    }

    fun decrypt(
        passphrase: String,
        blob: BackupBlob,
    ): ByteArray {
        require(passphrase.length >= MIN_PASSPHRASE) { "passphrase_too_short" }
        val key = derive(passphrase, blob.salt)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        return try {
            cipher.init(
                Cipher.DECRYPT_MODE,
                SecretKeySpec(key, "AES"),
                GCMParameterSpec(128, blob.nonce),
            )
            cipher.doFinal(blob.ciphertext)
        } catch (e: GeneralSecurityException) {
            throw GeneralSecurityException("wrong_passphrase", e)
        }
    }

    private fun derive(
        passphrase: String,
        salt: ByteArray,
    ): ByteArray {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(passphrase.toCharArray(), salt, ITERATIONS, 256)
        return factory.generateSecret(spec).encoded
    }
}

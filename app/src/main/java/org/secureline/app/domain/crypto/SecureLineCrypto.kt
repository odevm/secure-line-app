package org.secureline.app.domain.crypto

import com.google.crypto.tink.subtle.Ed25519Sign
import com.google.crypto.tink.subtle.Ed25519Verify
import com.google.crypto.tink.subtle.Hex
import com.google.crypto.tink.subtle.Random
import com.google.crypto.tink.subtle.X25519
import java.security.GeneralSecurityException
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

data class AeadCiphertext(
    val nonce: ByteArray,
    val ciphertext: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AeadCiphertext) return false
        return nonce.contentEquals(other.nonce) && ciphertext.contentEquals(other.ciphertext)
    }

    override fun hashCode(): Int = 31 * nonce.contentHashCode() + ciphertext.contentHashCode()
}

/**
 * SecureLine identity and AEAD helpers (X25519 ECDH, Ed25519, AES-256-GCM).
 * Nonce reuse for the same key is rejected by [NonceTracker].
 */
object SecureLineCrypto {
    private const val AES_KEY_BYTES = 32
    const val NONCE_BYTES = 12

    fun generateIdentity(): LocalIdentity {
        val xPriv = X25519.generatePrivateKey()
        val xPub = X25519.publicFromPrivate(xPriv)
        val edKeyPair = Ed25519Sign.KeyPair.newKeyPair()
        return LocalIdentity(
            localUserId = LocalIdentity.newId(),
            x25519PrivateKey = xPriv,
            x25519PublicKey = xPub,
            ed25519PrivateKey = edKeyPair.privateKey,
            ed25519PublicKey = edKeyPair.publicKey,
        )
    }

    fun agree(
        privateKey: ByteArray,
        peerPublicKey: ByteArray,
    ): ByteArray = X25519.computeSharedSecret(privateKey, peerPublicKey)

    fun deriveAesKey(
        sharedSecret: ByteArray,
        info: ByteArray = "secureline-aead-v1".toByteArray(),
    ): ByteArray {
        val digest = MessageDigest.getInstance("SHA-256")
        digest.update(sharedSecret)
        digest.update(info)
        return digest.digest().copyOf(AES_KEY_BYTES)
    }

    fun fingerprintShort(
        x25519Public: ByteArray,
        ed25519Public: ByteArray,
    ): String {
        val digest = MessageDigest.getInstance("SHA-256")
        digest.update(x25519Public)
        digest.update(ed25519Public)
        return Hex.encode(digest.digest().copyOf(4)).uppercase()
    }

    fun encrypt(
        aesKey: ByteArray,
        plaintext: ByteArray,
        aad: ByteArray = ByteArray(0),
        nonceTracker: NonceTracker = NonceTracker(),
        nonce: ByteArray = Random.randBytes(NONCE_BYTES),
    ): AeadCiphertext {
        require(aesKey.size == AES_KEY_BYTES) { "aes_key_size" }
        require(nonce.size == NONCE_BYTES) { "nonce_size" }
        nonceTracker.registerOrThrow(aesKey, nonce)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(aesKey, "AES"), GCMParameterSpec(128, nonce))
        if (aad.isNotEmpty()) {
            cipher.updateAAD(aad)
        }
        return AeadCiphertext(nonce = nonce.copyOf(), ciphertext = cipher.doFinal(plaintext))
    }

    fun decrypt(
        aesKey: ByteArray,
        payload: AeadCiphertext,
        aad: ByteArray = ByteArray(0),
    ): ByteArray {
        require(aesKey.size == AES_KEY_BYTES) { "aes_key_size" }
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(
            Cipher.DECRYPT_MODE,
            SecretKeySpec(aesKey, "AES"),
            GCMParameterSpec(128, payload.nonce),
        )
        if (aad.isNotEmpty()) {
            cipher.updateAAD(aad)
        }
        return try {
            cipher.doFinal(payload.ciphertext)
        } catch (e: GeneralSecurityException) {
            throw GeneralSecurityException("aead_tamper_or_bad_key", e)
        }
    }

    fun sign(
        ed25519Private: ByteArray,
        message: ByteArray,
    ): ByteArray = Ed25519Sign(ed25519Private).sign(message)

    fun verify(
        ed25519Public: ByteArray,
        message: ByteArray,
        signature: ByteArray,
    ): Boolean =
        try {
            Ed25519Verify(ed25519Public).verify(signature, message)
            true
        } catch (_: GeneralSecurityException) {
            false
        }
}

/**
 * Rejects AES-GCM nonce reuse for a given key within a session.
 */
class NonceTracker {
    private val used = HashSet<String>()

    fun registerOrThrow(
        key: ByteArray,
        nonce: ByteArray,
    ) {
        val token = Hex.encode(key) + ":" + Hex.encode(nonce)
        if (!used.add(token)) {
            throw GeneralSecurityException("nonce_reuse_rejected")
        }
    }
}

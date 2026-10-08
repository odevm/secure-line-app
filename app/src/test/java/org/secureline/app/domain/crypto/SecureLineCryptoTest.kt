package org.secureline.app.domain.crypto

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.security.GeneralSecurityException

class SecureLineCryptoTest {
    @Test
    fun ecdh_agreement_isSymmetric() {
        val alice = SecureLineCrypto.generateIdentity()
        val bob = SecureLineCrypto.generateIdentity()
        val ab = SecureLineCrypto.agree(alice.x25519PrivateKey, bob.x25519PublicKey)
        val ba = SecureLineCrypto.agree(bob.x25519PrivateKey, alice.x25519PublicKey)
        assertThat(ab).isEqualTo(ba)
        assertThat(alice.localUserId).isNotEqualTo(bob.localUserId)
    }

    @Test
    fun aesGcm_roundTrip_andTamperFails() {
        val alice = SecureLineCrypto.generateIdentity()
        val bob = SecureLineCrypto.generateIdentity()
        val shared = SecureLineCrypto.agree(alice.x25519PrivateKey, bob.x25519PublicKey)
        val key = SecureLineCrypto.deriveAesKey(shared)
        val tracker = NonceTracker()
        val payload =
            SecureLineCrypto.encrypt(
                aesKey = key,
                plaintext = "hello-secureline".toByteArray(),
                aad = "aad".toByteArray(),
                nonceTracker = tracker,
            )
        val plain =
            SecureLineCrypto.decrypt(
                aesKey = key,
                payload = payload,
                aad = "aad".toByteArray(),
            )
        assertThat(String(plain)).isEqualTo("hello-secureline")

        val tampered =
            payload.copy(
                ciphertext =
                    payload.ciphertext.copyOf().also {
                        it[0] = (it[0].toInt() xor 0xFF).toByte()
                    },
            )
        assertThrows(GeneralSecurityException::class.java) {
            SecureLineCrypto.decrypt(key, tampered, "aad".toByteArray())
        }
    }

    @Test
    fun nonceReuse_isRejected() {
        val key = ByteArray(32) { 7 }
        val nonce = ByteArray(SecureLineCrypto.NONCE_BYTES) { 1 }
        val tracker = NonceTracker()
        SecureLineCrypto.encrypt(
            aesKey = key,
            plaintext = byteArrayOf(1),
            nonceTracker = tracker,
            nonce = nonce,
        )
        assertThrows(GeneralSecurityException::class.java) {
            SecureLineCrypto.encrypt(
                aesKey = key,
                plaintext = byteArrayOf(2),
                nonceTracker = tracker,
                nonce = nonce,
            )
        }
    }

    @Test
    fun ed25519_signVerify() {
        val id = SecureLineCrypto.generateIdentity()
        val msg = "pair-confirm".toByteArray()
        val sig = SecureLineCrypto.sign(id.ed25519PrivateKey, msg)
        assertThat(SecureLineCrypto.verify(id.ed25519PublicKey, msg, sig)).isTrue()
        assertThat(SecureLineCrypto.verify(id.ed25519PublicKey, "other".toByteArray(), sig)).isFalse()
    }
}

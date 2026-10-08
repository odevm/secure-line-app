package org.secureline.app.domain.crypto

import java.util.UUID

data class LocalIdentity(
    val localUserId: String,
    val x25519PrivateKey: ByteArray,
    val x25519PublicKey: ByteArray,
    val ed25519PrivateKey: ByteArray,
    val ed25519PublicKey: ByteArray,
) {
    companion object {
        fun newId(): String = UUID.randomUUID().toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LocalIdentity) return false
        return localUserId == other.localUserId &&
            x25519PrivateKey.contentEquals(other.x25519PrivateKey) &&
            x25519PublicKey.contentEquals(other.x25519PublicKey) &&
            ed25519PrivateKey.contentEquals(other.ed25519PrivateKey) &&
            ed25519PublicKey.contentEquals(other.ed25519PublicKey)
    }

    override fun hashCode(): Int = localUserId.hashCode()
}

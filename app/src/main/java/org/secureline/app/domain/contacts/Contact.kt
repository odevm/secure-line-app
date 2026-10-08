package org.secureline.app.domain.contacts

data class Contact(
    val peerLocalUserId: String,
    val displayName: String = "",
    val x25519PublicKey: ByteArray,
    val ed25519PublicKey: ByteArray,
    val keyFingerprint: String,
    val verified: Boolean = false,
    val phoneNumbers: List<String> = emptyList(),
    val peerFirebaseUid: String? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Contact) return false
        return peerLocalUserId == other.peerLocalUserId
    }

    override fun hashCode(): Int = peerLocalUserId.hashCode()
}

package org.secureline.app.domain.contacts

/**
 * In-memory contact store keyed by [Contact.peerLocalUserId].
 * Phone numbers are transport endpoints only — never crypto identity.
 */
class ContactBook {
    private val byId = LinkedHashMap<String, Contact>()
    private val phoneIndex = HashMap<String, String>()
    private val firebaseIndex = HashMap<String, String>()

    fun upsert(contact: Contact): Contact {
        val existing = byId[contact.peerLocalUserId]
        val merged =
            if (existing == null) {
                contact.copy(phoneNumbers = contact.phoneNumbers.distinct())
            } else {
                val numbers = (existing.phoneNumbers + contact.phoneNumbers).distinct()
                existing.copy(
                    displayName = contact.displayName.ifBlank { existing.displayName },
                    x25519PublicKey = contact.x25519PublicKey,
                    ed25519PublicKey = contact.ed25519PublicKey,
                    keyFingerprint = contact.keyFingerprint,
                    verified = existing.verified || contact.verified,
                    phoneNumbers = numbers,
                    peerFirebaseUid = contact.peerFirebaseUid ?: existing.peerFirebaseUid,
                )
            }
        byId[merged.peerLocalUserId] = merged
        rebuildIndexes()
        return merged
    }

    fun get(peerLocalUserId: String): Contact? = byId[peerLocalUserId]

    fun findByPhone(e164: String): Contact? = phoneIndex[e164]?.let { byId[it] }

    fun findByFirebaseUid(uid: String): Contact? = firebaseIndex[uid]?.let { byId[it] }

    fun all(): List<Contact> = byId.values.toList()

    private fun rebuildIndexes() {
        phoneIndex.clear()
        firebaseIndex.clear()
        for (c in byId.values) {
            for (n in c.phoneNumbers) {
                phoneIndex[n] = c.peerLocalUserId
            }
            c.peerFirebaseUid?.let { firebaseIndex[it] = c.peerLocalUserId }
        }
    }
}

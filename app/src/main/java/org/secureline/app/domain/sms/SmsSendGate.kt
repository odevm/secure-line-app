package org.secureline.app.domain.sms

import org.secureline.app.domain.contacts.Contact

sealed class SmsSendDecision {
    data object Allow : SmsSendDecision()

    data class Block(
        val reason: String,
    ) : SmsSendDecision()
}

object SmsSendGate {
    fun decide(contact: Contact?): SmsSendDecision {
        if (contact == null) return SmsSendDecision.Block("unknown_contact")
        if (contact.keyFingerprint.isBlank()) return SmsSendDecision.Block("missing_fingerprint")
        // Fingerprint mismatch is signaled by callers via a dedicated flag.
        return SmsSendDecision.Allow
    }

    fun decide(
        contact: Contact?,
        liveFingerprint: String,
    ): SmsSendDecision {
        val base = decide(contact)
        if (base is SmsSendDecision.Block) return base
        requireNotNull(contact)
        if (liveFingerprint != contact.keyFingerprint) {
            return SmsSendDecision.Block("fingerprint_mismatch")
        }
        return SmsSendDecision.Allow
    }
}

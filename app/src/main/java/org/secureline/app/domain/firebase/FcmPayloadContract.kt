package org.secureline.app.domain.firebase

/**
 * Console-only FCM contract: payloads must never carry E2EE content or keys.
 */
object FcmPayloadContract {
    private val DENYLIST =
        listOf(
            "private_key",
            "session_key",
            "plaintext",
            "sms_body",
            "sas",
            "pin",
            "passphrase",
        )

    fun isAllowed(data: Map<String, String>): Boolean {
        for ((key, value) in data) {
            val k = key.lowercase()
            if (DENYLIST.any { it in k }) return false
            val v = value.lowercase()
            if (DENYLIST.any { it in v }) return false
        }
        return true
    }
}

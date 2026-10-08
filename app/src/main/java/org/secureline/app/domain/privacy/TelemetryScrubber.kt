package org.secureline.app.domain.privacy

/**
 * Scrubs Analytics / Crashlytics payloads. Upload only after consent.
 */
object TelemetryScrubber {
    private val DENY =
        listOf(
            "private_key",
            "session_key",
            "plaintext",
            "sms_body",
            "sas",
            "pin",
            "passphrase",
            "nonce",
            "x25519",
            "ed25519",
        )

    fun mayUpload(consentGranted: Boolean): Boolean = consentGranted

    fun scrub(params: Map<String, String>): Map<String, String> =
        params.filterKeys { key ->
            val k = key.lowercase()
            DENY.none { it in k }
        }.mapValues { (_, v) ->
            val lower = v.lowercase()
            if (DENY.any { it in lower }) "[redacted]" else v
        }

    fun isDeniedKey(key: String): Boolean {
        val k = key.lowercase()
        return DENY.any { it in k }
    }
}

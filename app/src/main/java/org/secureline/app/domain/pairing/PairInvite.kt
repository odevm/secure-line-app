package org.secureline.app.domain.pairing

/**
 * Public pairing payload carried in QR / `secureline://pair` URIs.
 * Never includes private keys or AEAD session material.
 */
data class PairInvite(
    val version: Int,
    val localUserId: String,
    val x25519PublicKeyB64Url: String,
    val ed25519PublicKeyB64Url: String,
    val fingerprintShort: String,
    val firebaseUid: String? = null,
    val phoneNumbersE164: List<String> = emptyList(),
)

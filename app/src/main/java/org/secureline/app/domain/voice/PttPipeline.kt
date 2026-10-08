package org.secureline.app.domain.voice

import org.secureline.app.domain.crypto.NonceTracker
import org.secureline.app.domain.crypto.SecureLineCrypto
import org.secureline.app.domain.session.SecureSessionState

/**
 * Secure PTT path: Codec2 bytes → AES-GCM → modem symbols (domain orchestration).
 */
class PttPipeline(
    private val sessionKey: ByteArray,
    private val encodeCodec: (ShortArray) -> ByteArray,
    private val decodeCodec: (ByteArray) -> ShortArray,
    private val modemEncode: (ByteArray) -> ByteArray,
    private val modemDecode: (ByteArray) -> ByteArray,
) {
    private val tracker = NonceTracker()
    var pressing: Boolean = false
        private set

    fun canTransmit(state: SecureSessionState): Boolean = state == SecureSessionState.PttReady

    fun press(): Boolean {
        pressing = true
        return true
    }

    fun release() {
        pressing = false
    }

    fun transmitPcm(
        state: SecureSessionState,
        pcm: ShortArray,
    ): ByteArray {
        require(canTransmit(state)) { "ptt_not_ready" }
        require(pressing) { "ptt_not_pressed" }
        val codecFrame = encodeCodec(pcm)
        val sealed = SecureLineCrypto.encrypt(sessionKey, codecFrame, nonceTracker = tracker)
        val payload = sealed.nonce + sealed.ciphertext
        return modemEncode(payload)
    }

    fun receiveSymbols(symbols: ByteArray): ShortArray {
        val payload = modemDecode(symbols)
        require(payload.size > SecureLineCrypto.NONCE_BYTES) { "short_frame" }
        val nonce = payload.copyOfRange(0, SecureLineCrypto.NONCE_BYTES)
        val ct = payload.copyOfRange(SecureLineCrypto.NONCE_BYTES, payload.size)
        val codecFrame =
            SecureLineCrypto.decrypt(
                sessionKey,
                org.secureline.app.domain.crypto.AeadCiphertext(nonce, ct),
            )
        return decodeCodec(codecFrame)
    }
}

package org.secureline.app.domain.voice

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.secureline.app.domain.session.SecureSessionState

class PttPipelineTest {
    private fun pipeline(): PttPipeline {
        val key = ByteArray(32) { 3 }
        return PttPipeline(
            sessionKey = key,
            encodeCodec = { pcm -> ByteArray(6) { (pcm.getOrElse(it) { 0 }).toByte() } },
            decodeCodec = { bytes -> ShortArray(160) { bytes.getOrElse(it % bytes.size) { 1 }.toShort() } },
            modemEncode = { it },
            modemDecode = { it },
        )
    }

    @Test
    fun pressState_andRoundTripWhenReady() {
        val p = pipeline()
        assertThat(p.canTransmit(SecureSessionState.SasGate)).isFalse()
        assertThrows(IllegalArgumentException::class.java) {
            p.transmitPcm(SecureSessionState.PttReady, ShortArray(160) { 10 })
        }
        p.press()
        val symbols = p.transmitPcm(SecureSessionState.PttReady, ShortArray(160) { 10 })
        assertThat(symbols).isNotEmpty()
        val pcm = p.receiveSymbols(symbols)
        assertThat(pcm).hasLength(160)
        p.release()
        assertThat(p.pressing).isFalse()
    }
}

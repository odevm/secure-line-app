package org.secureline.app.domain.sms

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.UUID

class SmsFramerTest {
    @Test
    fun multipartRoundTrip() {
        val peer = UUID.randomUUID().toString()
        val key = ByteArray(32) { 9 }
        val plain = "hello ".repeat(40).toByteArray()
        val messages = SmsFramer.encryptAndChunk(peer, plain, key)
        assertThat(messages.size).isGreaterThan(1)
        assertThat(messages.all { it.startsWith(SmsFramer.PREFIX) }).isTrue()
        val out = SmsFramer.decryptAndReassemble(messages, peer, key)
        assertThat(out).isEqualTo(plain)
    }

    @Test
    fun wrongKey_failClosed() {
        val peer = UUID.randomUUID().toString()
        val messages =
            SmsFramer.encryptAndChunk(
                peer,
                "secret".toByteArray(),
                ByteArray(32) { 1 },
            )
        assertThrows(SecurityException::class.java) {
            SmsFramer.decryptAndReassemble(messages, peer, ByteArray(32) { 2 })
        }
    }
}

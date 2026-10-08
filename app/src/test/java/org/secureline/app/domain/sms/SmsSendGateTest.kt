package org.secureline.app.domain.sms

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.secureline.app.domain.contacts.Contact

class SmsSendGateTest {
    private fun contact(fp: String = "ABCD") =
        Contact(
            peerLocalUserId = "peer",
            x25519PublicKey = ByteArray(32),
            ed25519PublicKey = ByteArray(32),
            keyFingerprint = fp,
            phoneNumbers = listOf("+1555"),
        )

    @Test
    fun mismatch_blocksSend() {
        val decision = SmsSendGate.decide(contact("AAAA"), liveFingerprint = "BBBB")
        assertThat(decision).isEqualTo(SmsSendDecision.Block("fingerprint_mismatch"))
    }

    @Test
    fun match_allowsSend() {
        assertThat(SmsSendGate.decide(contact("AAAA"), "AAAA")).isEqualTo(SmsSendDecision.Allow)
    }
}

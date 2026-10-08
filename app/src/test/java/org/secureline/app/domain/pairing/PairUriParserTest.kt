package org.secureline.app.domain.pairing

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class PairUriParserTest {
    private val sample =
        PairInvite(
            version = 1,
            localUserId = "11111111-2222-3333-4444-555555555555",
            x25519PublicKeyB64Url = "x25519pubkey",
            ed25519PublicKeyB64Url = "ed25519pubkey",
            fingerprintShort = "ABCD1234",
            firebaseUid = "firebase-uid-1",
            phoneNumbersE164 = listOf("+15551212", "+15559999"),
        )

    @Test
    fun roundTrip_preservesInvite() {
        val uri = PairUriParser.generate(sample)
        val result = PairUriParser.parse(uri)
        assertThat(result).isInstanceOf(PairUriResult.Ok::class.java)
        val invite = (result as PairUriResult.Ok).invite
        assertThat(invite.localUserId).isEqualTo(sample.localUserId)
        assertThat(invite.x25519PublicKeyB64Url).isEqualTo(sample.x25519PublicKeyB64Url)
        assertThat(invite.ed25519PublicKeyB64Url).isEqualTo(sample.ed25519PublicKeyB64Url)
        assertThat(invite.fingerprintShort).isEqualTo(sample.fingerprintShort)
        assertThat(invite.firebaseUid).isEqualTo(sample.firebaseUid)
        assertThat(invite.phoneNumbersE164).containsExactlyElementsIn(sample.phoneNumbersE164)
            .inOrder()
    }

    @Test
    fun optionalFirebaseUid_mayBeAbsent() {
        val withoutFid = sample.copy(firebaseUid = null, phoneNumbersE164 = emptyList())
        val parsed = PairUriParser.parse(PairUriParser.generate(withoutFid)) as PairUriResult.Ok
        assertThat(parsed.invite.firebaseUid).isNull()
        assertThat(parsed.invite.phoneNumbersE164).isEmpty()
    }

    @Test
    fun reject_missingLid() {
        val raw =
            "secureline://pair?v=1&x25519=a&ed25519=b&fp=c"
        val result = PairUriParser.parse(raw)
        assertThat(result).isEqualTo(PairUriResult.Err("missing_lid"))
    }

    @Test
    fun reject_psk() {
        val raw =
            "secureline://pair?v=1&lid=x&x25519=a&ed25519=b&fp=c&psk=nope"
        assertThat(PairUriParser.parse(raw)).isEqualTo(PairUriResult.Err("psk_rejected"))
    }

    @Test
    fun reject_badVersionAndHost() {
        assertThat(
            PairUriParser.parse("secureline://pair?v=99&lid=x&x25519=a&ed25519=b&fp=c"),
        ).isEqualTo(PairUriResult.Err("unsupported_v"))
        assertThat(
            PairUriParser.parse("secureline://evil?v=1&lid=x&x25519=a&ed25519=b&fp=c"),
        ).isEqualTo(PairUriResult.Err("bad_host"))
        assertThat(
            PairUriParser.parse("https://pair?v=1&lid=x&x25519=a&ed25519=b&fp=c"),
        ).isEqualTo(PairUriResult.Err("bad_scheme"))
    }
}

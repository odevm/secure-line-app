package org.secureline.app.domain.pairing

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class HandshakeFrameCodecTest {
    @Test
    fun roundTrip_withOptionalFirebaseUid() {
        val frame =
            HandshakeFrame(
                localUserId = UUID.randomUUID().toString(),
                x25519PublicKey = ByteArray(32) { it.toByte() },
                ed25519PublicKey = ByteArray(32) { (255 - it).toByte() },
                firebaseUid = "firebase-abc",
            )
        val packed = HandshakeFrameCodec.pack(frame)
        val result = HandshakeFrameCodec.unpack(packed)
        assertThat(result).isInstanceOf(HandshakeCodecResult.Ok::class.java)
        assertThat((result as HandshakeCodecResult.Ok).frame).isEqualTo(frame)
    }

    @Test
    fun roundTrip_withoutFirebaseUid() {
        val frame =
            HandshakeFrame(
                localUserId = UUID.randomUUID().toString(),
                x25519PublicKey = ByteArray(32) { 1 },
                ed25519PublicKey = ByteArray(32) { 2 },
                firebaseUid = null,
            )
        val unpacked = HandshakeFrameCodec.unpack(HandshakeFrameCodec.pack(frame)) as HandshakeCodecResult.Ok
        assertThat(unpacked.frame.firebaseUid).isNull()
        assertThat(unpacked.frame.localUserId).isEqualTo(frame.localUserId)
    }

    @Test
    fun reject_badMagic() {
        val frame =
            HandshakeFrame(
                localUserId = UUID.randomUUID().toString(),
                x25519PublicKey = ByteArray(32),
                ed25519PublicKey = ByteArray(32),
            )
        val packed = HandshakeFrameCodec.pack(frame)
        packed[0] = 'X'.code.toByte()
        assertThat(HandshakeFrameCodec.unpack(packed))
            .isEqualTo(HandshakeCodecResult.Err("bad_magic"))
    }
}

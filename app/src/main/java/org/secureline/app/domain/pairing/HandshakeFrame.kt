package org.secureline.app.domain.pairing

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets
import java.util.UUID

/**
 * TOFU modem handshake: localUserId + public keys (+ optional firebaseUid).
 * Wire format v1 (big-endian):
 * magic(4)='SLH1' | version(u8)=1 | flags(u8) | lid(16 uuid bytes) |
 * x25519(32) | ed25519(32) | [fid_len(u8)+fid_utf8] when FLAG_FID set.
 */
data class HandshakeFrame(
    val localUserId: String,
    val x25519PublicKey: ByteArray,
    val ed25519PublicKey: ByteArray,
    val firebaseUid: String? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HandshakeFrame) return false
        return localUserId == other.localUserId &&
            x25519PublicKey.contentEquals(other.x25519PublicKey) &&
            ed25519PublicKey.contentEquals(other.ed25519PublicKey) &&
            firebaseUid == other.firebaseUid
    }

    override fun hashCode(): Int =
        31 * localUserId.hashCode() + x25519PublicKey.contentHashCode()
}

sealed class HandshakeCodecResult {
    data class Ok(
        val frame: HandshakeFrame,
    ) : HandshakeCodecResult()

    data class Err(
        val reason: String,
    ) : HandshakeCodecResult()
}

object HandshakeFrameCodec {
    private val MAGIC = byteArrayOf('S'.code.toByte(), 'L'.code.toByte(), 'H'.code.toByte(), '1'.code.toByte())
    private const val VERSION: Byte = 1
    private const val FLAG_FID: Int = 0x01
    private const val KEY_LEN = 32
    private const val MAX_FID = 128

    fun pack(frame: HandshakeFrame): ByteArray {
        require(frame.x25519PublicKey.size == KEY_LEN)
        require(frame.ed25519PublicKey.size == KEY_LEN)
        val uuid = UUID.fromString(frame.localUserId)
        val fidBytes = frame.firebaseUid?.toByteArray(StandardCharsets.UTF_8)
        require(fidBytes == null || fidBytes.size <= MAX_FID)
        val flags = if (fidBytes != null) FLAG_FID else 0
        val size = 4 + 1 + 1 + 16 + KEY_LEN + KEY_LEN + if (fidBytes != null) 1 + fidBytes.size else 0
        val buf = ByteBuffer.allocate(size).order(ByteOrder.BIG_ENDIAN)
        buf.put(MAGIC)
        buf.put(VERSION)
        buf.put(flags.toByte())
        buf.putLong(uuid.mostSignificantBits)
        buf.putLong(uuid.leastSignificantBits)
        buf.put(frame.x25519PublicKey)
        buf.put(frame.ed25519PublicKey)
        if (fidBytes != null) {
            buf.put(fidBytes.size.toByte())
            buf.put(fidBytes)
        }
        return buf.array()
    }

    fun unpack(bytes: ByteArray): HandshakeCodecResult {
        if (bytes.size < 4 + 1 + 1 + 16 + KEY_LEN + KEY_LEN) {
            return HandshakeCodecResult.Err("too_short")
        }
        val buf = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN)
        val magic = ByteArray(4)
        buf.get(magic)
        if (!magic.contentEquals(MAGIC)) {
            return HandshakeCodecResult.Err("bad_magic")
        }
        val version = buf.get()
        if (version != VERSION) {
            return HandshakeCodecResult.Err("bad_version")
        }
        val flags = buf.get().toInt() and 0xFF
        val msb = buf.long
        val lsb = buf.long
        val lid = UUID(msb, lsb).toString()
        val x = ByteArray(KEY_LEN)
        val e = ByteArray(KEY_LEN)
        buf.get(x)
        buf.get(e)
        var fid: String? = null
        if ((flags and FLAG_FID) != 0) {
            if (buf.remaining() < 1) {
                return HandshakeCodecResult.Err("truncated_fid")
            }
            val len = buf.get().toInt() and 0xFF
            if (len > MAX_FID || buf.remaining() < len) {
                return HandshakeCodecResult.Err("bad_fid_len")
            }
            val fidRaw = ByteArray(len)
            buf.get(fidRaw)
            fid = String(fidRaw, StandardCharsets.UTF_8)
        }
        return HandshakeCodecResult.Ok(
            HandshakeFrame(
                localUserId = lid,
                x25519PublicKey = x,
                ed25519PublicKey = e,
                firebaseUid = fid,
            ),
        )
    }
}

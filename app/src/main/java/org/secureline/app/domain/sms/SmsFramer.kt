package org.secureline.app.domain.sms

import org.secureline.app.domain.crypto.AeadCiphertext
import org.secureline.app.domain.crypto.NonceTracker
import org.secureline.app.domain.crypto.SecureLineCrypto
import java.nio.ByteBuffer
import java.util.Base64
import java.util.UUID

/**
 * Encrypted SMS wire format: `SL1:` + base64url(header||nonce||ciphertext).
 * Header: version(u8)=1 | peerLocalUserId UUID(16) | chunkIndex(u8) | chunkCount(u8).
 */
object SmsFramer {
    const val PREFIX = "SL1:"
    private const val VERSION: Byte = 1
    private const val MAX_CHUNK_PLAINTEXT = 80

    fun encryptAndChunk(
        peerLocalUserId: String,
        plaintext: ByteArray,
        aesKey: ByteArray,
        nonceTracker: NonceTracker = NonceTracker(),
    ): List<String> {
        val chunks = plaintext.toList().chunked(MAX_CHUNK_PLAINTEXT).map { it.toByteArray() }
        val count = chunks.size.coerceAtLeast(1)
        val effective = if (chunks.isEmpty()) listOf(ByteArray(0)) else chunks
        return effective.mapIndexed { index, part ->
            val aad = aadBytes(peerLocalUserId, index, count)
            val sealed = SecureLineCrypto.encrypt(aesKey, part, aad, nonceTracker)
            PREFIX + encodeFrame(peerLocalUserId, index, count, sealed)
        }
    }

    fun decryptAndReassemble(
        messages: List<String>,
        expectedPeerLocalUserId: String,
        aesKey: ByteArray,
    ): ByteArray {
        val parts = ArrayList<Pair<Int, ByteArray>>()
        var total = -1
        for (msg in messages) {
            val frame = decodeFrame(msg) ?: throw SecurityException("bad_frame")
            if (frame.peerLocalUserId != expectedPeerLocalUserId) {
                throw SecurityException("peer_mismatch")
            }
            if (total < 0) total = frame.chunkCount
            if (frame.chunkCount != total) throw SecurityException("count_mismatch")
            val aad = aadBytes(frame.peerLocalUserId, frame.chunkIndex, frame.chunkCount)
            val plain =
                try {
                    SecureLineCrypto.decrypt(aesKey, frame.payload, aad)
                } catch (_: Exception) {
                    throw SecurityException("decrypt_failed")
                }
            parts += frame.chunkIndex to plain
        }
        if (total < 0) throw SecurityException("empty")
        if (parts.size != total) throw SecurityException("incomplete")
        return parts.sortedBy { it.first }.map { it.second }.reduce { a, b -> a + b }
    }

    private fun aadBytes(
        peerLocalUserId: String,
        index: Int,
        count: Int,
    ): ByteArray {
        val uuid = UUID.fromString(peerLocalUserId)
        return ByteBuffer
            .allocate(16 + 2)
            .putLong(uuid.mostSignificantBits)
            .putLong(uuid.leastSignificantBits)
            .put(index.toByte())
            .put(count.toByte())
            .array()
    }

    private fun encodeFrame(
        peerLocalUserId: String,
        index: Int,
        count: Int,
        sealed: AeadCiphertext,
    ): String {
        val uuid = UUID.fromString(peerLocalUserId)
        val buf =
            ByteBuffer.allocate(1 + 16 + 1 + 1 + sealed.nonce.size + sealed.ciphertext.size)
        buf.put(VERSION)
        buf.putLong(uuid.mostSignificantBits)
        buf.putLong(uuid.leastSignificantBits)
        buf.put(index.toByte())
        buf.put(count.toByte())
        buf.put(sealed.nonce)
        buf.put(sealed.ciphertext)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buf.array())
    }

    private data class Decoded(
        val peerLocalUserId: String,
        val chunkIndex: Int,
        val chunkCount: Int,
        val payload: AeadCiphertext,
    )

    private fun decodeFrame(message: String): Decoded? {
        if (!message.startsWith(PREFIX)) return null
        val raw =
            try {
                Base64.getUrlDecoder().decode(message.removePrefix(PREFIX))
            } catch (_: Exception) {
                return null
            }
        if (raw.size < 1 + 16 + 1 + 1 + SecureLineCrypto.NONCE_BYTES + 16) return null
        val buf = ByteBuffer.wrap(raw)
        if (buf.get() != VERSION) return null
        val uuid = UUID(buf.long, buf.long).toString()
        val index = buf.get().toInt() and 0xFF
        val count = buf.get().toInt() and 0xFF
        val nonce = ByteArray(SecureLineCrypto.NONCE_BYTES)
        buf.get(nonce)
        val ct = ByteArray(buf.remaining())
        buf.get(ct)
        return Decoded(uuid, index, count, AeadCiphertext(nonce, ct))
    }
}

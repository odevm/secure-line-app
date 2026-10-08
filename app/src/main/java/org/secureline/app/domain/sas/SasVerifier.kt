package org.secureline.app.domain.sas

import java.nio.charset.StandardCharsets
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

data class SasMaterial(
    val emojiIndices: List<Int>,
    val digitGroups: List<String>,
) {
    fun emojiCodes(): List<String> = emojiIndices.map { SAS_EMOJI[it] }
}

/**
 * Telegram-style SAS: 4 emoji + 2×4 decimal digits from HKDF-SHA256(session, info).
 */
object SasVerifier {
    const val INFO = "secureline-sas-v1"
    private const val EMOJI_COUNT = 4
    private const val DIGIT_GROUPS = 2
    private const val DIGITS_PER_GROUP = 4

    fun derive(sessionSecret: ByteArray): SasMaterial {
        val okm = hkdfSha256(sessionSecret, INFO.toByteArray(StandardCharsets.UTF_8), 32)
        val emoji =
            (0 until EMOJI_COUNT).map { i ->
                (okm[i].toInt() and 0xFF) % SAS_EMOJI.size
            }
        val digits = ArrayList<String>(DIGIT_GROUPS)
        var offset = EMOJI_COUNT
        repeat(DIGIT_GROUPS) {
            var value = 0
            repeat(2) {
                value = (value shl 8) or (okm[offset].toInt() and 0xFF)
                offset += 1
            }
            val mod = 10_000
            digits += (value % mod).toString().padStart(DIGITS_PER_GROUP, '0')
        }
        return SasMaterial(emojiIndices = emoji, digitGroups = digits)
    }

    fun matches(
        a: SasMaterial,
        b: SasMaterial,
    ): Boolean = a.emojiIndices == b.emojiIndices && a.digitGroups == b.digitGroups

    private fun hkdfSha256(
        ikm: ByteArray,
        info: ByteArray,
        length: Int,
    ): ByteArray {
        val salt = ByteArray(32)
        val prk = hmacSha256(salt, ikm)
        val result = ByteArray(length)
        var t = ByteArray(0)
        var offset = 0
        var counter: Byte = 1
        while (offset < length) {
            val mac = Mac.getInstance("HmacSHA256")
            mac.init(SecretKeySpec(prk, "HmacSHA256"))
            mac.update(t)
            mac.update(info)
            mac.update(counter)
            t = mac.doFinal()
            val toCopy = minOf(t.size, length - offset)
            System.arraycopy(t, 0, result, offset, toCopy)
            offset += toCopy
            counter = (counter + 1).toByte()
        }
        return result
    }

    private fun hmacSha256(
        key: ByteArray,
        data: ByteArray,
    ): ByteArray {
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(key, "HmacSHA256"))
        return mac.doFinal(data)
    }
}

/** Fixed 25-symbol list (names for PROTOCOL.md; UI shows emoji). */
val SAS_EMOJI: List<String> =
    listOf(
        "🍎", "🚗", "🐶", "🌊", "⭐",
        "🎵", "🏠", "🌞", "🌙", "🔥",
        "🍀", "🎯", "🚀", "🔑", "📦",
        "🦊", "🌈", "⚡", "🧊", "🎈",
        "🧭", "📎", "🪴", "🥁", "🪙",
    )

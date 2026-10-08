package org.secureline.app.domain.pairing

import java.net.URI
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.Base64

sealed class PairUriResult {
    data class Ok(
        val invite: PairInvite,
    ) : PairUriResult()

    data class Err(
        val reason: String,
    ) : PairUriResult()
}

object PairUriParser {
    const val SCHEME = "secureline"
    const val HOST_PAIR = "pair"
    const val SUPPORTED_VERSION = 1

    fun parse(raw: String): PairUriResult {
        val uri =
            try {
                URI(raw.trim())
            } catch (_: Exception) {
                return PairUriResult.Err("malformed_uri")
            }
        if (!SCHEME.equals(uri.scheme, ignoreCase = true)) {
            return PairUriResult.Err("bad_scheme")
        }
        if (uri.host != HOST_PAIR) {
            return PairUriResult.Err("bad_host")
        }
        val query = uri.rawQuery ?: return PairUriResult.Err("missing_query")
        val params = parseQuery(query)
        if (params.containsKey("psk")) {
            return PairUriResult.Err("psk_rejected")
        }
        val version =
            params["v"]?.toIntOrNull()
                ?: return PairUriResult.Err("missing_or_bad_v")
        if (version != SUPPORTED_VERSION) {
            return PairUriResult.Err("unsupported_v")
        }
        val lid =
            params["lid"]?.takeIf { it.isNotBlank() }
                ?: return PairUriResult.Err("missing_lid")
        val x25519 =
            params["x25519"]?.takeIf { it.isNotBlank() }
                ?: return PairUriResult.Err("missing_x25519")
        val ed25519 =
            params["ed25519"]?.takeIf { it.isNotBlank() }
                ?: return PairUriResult.Err("missing_ed25519")
        val fp =
            params["fp"]?.takeIf { it.isNotBlank() }
                ?: return PairUriResult.Err("missing_fp")
        val fid = params["fid"]?.takeIf { it.isNotBlank() }
        val nums =
            params["nums"]
                ?.takeIf { it.isNotBlank() }
                ?.let { decodeNums(it) }
                ?: emptyList()
        return PairUriResult.Ok(
            PairInvite(
                version = version,
                localUserId = lid,
                x25519PublicKeyB64Url = x25519,
                ed25519PublicKeyB64Url = ed25519,
                fingerprintShort = fp,
                firebaseUid = fid,
                phoneNumbersE164 = nums,
            ),
        )
    }

    fun generate(invite: PairInvite): String {
        require(invite.version == SUPPORTED_VERSION)
        require(invite.localUserId.isNotBlank())
        val parts =
            mutableListOf(
                "v=${invite.version}",
                "lid=${enc(invite.localUserId)}",
                "x25519=${enc(invite.x25519PublicKeyB64Url)}",
                "ed25519=${enc(invite.ed25519PublicKeyB64Url)}",
                "fp=${enc(invite.fingerprintShort)}",
            )
        invite.firebaseUid?.takeIf { it.isNotBlank() }?.let {
            parts += "fid=${enc(it)}"
        }
        if (invite.phoneNumbersE164.isNotEmpty()) {
            parts += "nums=${enc(encodeNums(invite.phoneNumbersE164))}"
        }
        return "$SCHEME://$HOST_PAIR?${parts.joinToString("&")}"
    }

    private fun parseQuery(rawQuery: String): Map<String, String> =
        rawQuery
            .split("&")
            .filter { it.isNotBlank() }
            .associate { part ->
                val idx = part.indexOf('=')
                if (idx < 0) {
                    decode(part) to ""
                } else {
                    decode(part.substring(0, idx)) to decode(part.substring(idx + 1))
                }
            }

    private fun enc(value: String): String =
        URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20")

    private fun decode(value: String): String = URLDecoder.decode(value, StandardCharsets.UTF_8)

    private fun encodeNums(numbers: List<String>): String {
        val csv = numbers.joinToString(",")
        return Base64
            .getUrlEncoder()
            .withoutPadding()
            .encodeToString(csv.toByteArray(StandardCharsets.UTF_8))
    }

    private fun decodeNums(b64: String): List<String> =
        try {
            val csv = String(Base64.getUrlDecoder().decode(b64), StandardCharsets.UTF_8)
            csv.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        } catch (_: Exception) {
            emptyList()
        }
}

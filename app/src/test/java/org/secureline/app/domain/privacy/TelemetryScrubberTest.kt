package org.secureline.app.domain.privacy

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TelemetryScrubberTest {
    @Test
    fun noUploadBeforeConsent() {
        assertThat(TelemetryScrubber.mayUpload(false)).isFalse()
        assertThat(TelemetryScrubber.mayUpload(true)).isTrue()
    }

    @Test
    fun denylistScrubsSensitiveKeysAndValues() {
        val scrubbed =
            TelemetryScrubber.scrub(
                mapOf(
                    "screen" to "home",
                    "session_key" to "abc",
                    "note" to "contains sas leak",
                ),
            )
        assertThat(scrubbed).containsEntry("screen", "home")
        assertThat(scrubbed).doesNotContainKey("session_key")
        assertThat(scrubbed["note"]).isEqualTo("[redacted]")
        assertThat(TelemetryScrubber.isDeniedKey("private_key")).isTrue()
    }
}

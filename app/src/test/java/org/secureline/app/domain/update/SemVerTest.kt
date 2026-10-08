package org.secureline.app.domain.update

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class SemVerTest {
    @Test
    fun parse_acceptsVPrefixAndCompares() {
        assertThat(SemVer.parse("v0.2.0")).isEqualTo(SemVer(0, 2, 0))
        assertThat(SemVer.parse("0.1.0")).isEqualTo(SemVer(0, 1, 0))
        assertThat(SemVer.parse("latest")).isNull()
        assertThat(SemVer.parse("1.0")).isNull()
    }

    @Test
    fun updateChecker_ignoresBadTags() {
        assertThat(UpdateChecker.isNewer("v0.2.0", "0.1.0")).isTrue()
        assertThat(UpdateChecker.isNewer("v0.1.0", "0.1.0")).isFalse()
        assertThat(UpdateChecker.isNewer("nightly", "0.1.0")).isFalse()
    }
}

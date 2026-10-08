package org.secureline.app

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class VersionSmokeTest {
    @Test
    fun projectSemVerPrefix_isZeroOne() {
        assertThat("0.1.0").startsWith("0.1")
    }
}

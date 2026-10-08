package org.secureline.app

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

class SampleHarnessTest {
    interface VersionProvider {
        fun versionName(): String
    }

    @Test
    fun mockkAndTruth_workTogether() {
        val provider = mockk<VersionProvider>()
        every { provider.versionName() } returns "0.1.0"

        assertThat(provider.versionName()).isEqualTo("0.1.0")
    }
}

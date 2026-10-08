package org.secureline.app.nativebridge

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import java.io.File

/**
 * Ensures the native host round-trip binary exists in CI artifacts path convention.
 * Full bit-exact coverage runs via `native/tests` host executable (see TESTING.md).
 */
class ModemHostContractTest {
    @Test
    fun modemSources_arePresent() {
        val root = File("").absoluteFile
        // Gradle cwd is typically the module or project; search upward.
        val candidates =
            listOf(
                File(root, "native/modem/src/modem.c"),
                File(root, "../native/modem/src/modem.c"),
                File(root, "../../native/modem/src/modem.c"),
            )
        assertThat(candidates.any { it.isFile }).isTrue()
    }
}

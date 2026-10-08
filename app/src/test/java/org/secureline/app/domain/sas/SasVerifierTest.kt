package org.secureline.app.domain.sas

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class SasVerifierTest {
    @Test
    fun sameSecret_equalSas() {
        val secret = ByteArray(32) { (it * 7).toByte() }
        val a = SasVerifier.derive(secret)
        val b = SasVerifier.derive(secret.copyOf())
        assertThat(SasVerifier.matches(a, b)).isTrue()
        assertThat(a.emojiIndices).hasSize(4)
        assertThat(a.digitGroups).hasSize(2)
        assertThat(a.digitGroups[0]).hasLength(4)
        assertThat(a.emojiCodes()).hasSize(4)
        assertThat(SAS_EMOJI).hasSize(25)
    }

    @Test
    fun differentSecret_mismatch() {
        val a = SasVerifier.derive(ByteArray(32) { 1 })
        val b = SasVerifier.derive(ByteArray(32) { 2 })
        assertThat(SasVerifier.matches(a, b)).isFalse()
    }
}

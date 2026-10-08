package org.secureline.app.domain.root

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class RootAudioGateTest {
    @Test
    fun nonRootPath_unchangedWhenDenied() {
        val gate = RootAudioGate()
        gate.detect(suPresent = false)
        assertThat(gate.requestGrant(true)).isFalse()
        assertThat(gate.useRootAudioPath()).isFalse()

        gate.detect(suPresent = true)
        assertThat(gate.requestGrant(userConfirmed = false)).isFalse()
        assertThat(gate.useRootAudioPath()).isFalse()
        assertThat(gate.requestGrant(userConfirmed = true)).isTrue()
        assertThat(gate.useRootAudioPath()).isTrue()
        gate.denyOrRevoke()
        assertThat(gate.useRootAudioPath()).isFalse()
    }
}

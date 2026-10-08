package org.secureline.app.domain.audio

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class DeviceProfileTest {
    @Test
    fun loadSave_roundTripJson() {
        val store = DeviceProfileStore()
        val profile = DeviceProfile(deviceModel = "Pixel 8", modemGain = 1.2)
        store.save(profile)
        val json = store.exportJson("Pixel 8")!!
        val restored = store.importJson(json)
        assertThat(restored.deviceModel).isEqualTo("Pixel 8")
        assertThat(restored.modemGain).isWithin(0.001).of(1.2)
        assertThat(store.load("Pixel 8")).isEqualTo(restored)
    }
}

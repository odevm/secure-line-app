package org.secureline.app.ui.i18n

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class AppLocaleTest {
    @Test
    fun fromStorage_mapsKnownValues() {
        assertThat(AppLocale.fromStorage("en")).isEqualTo(AppLocale.ENGLISH)
        assertThat(AppLocale.fromStorage("ar")).isEqualTo(AppLocale.ARABIC)
        assertThat(AppLocale.fromStorage("system")).isEqualTo(AppLocale.SYSTEM)
    }

    @Test
    fun fromStorage_unknownFallsBackToSystem() {
        assertThat(AppLocale.fromStorage(null)).isEqualTo(AppLocale.SYSTEM)
        assertThat(AppLocale.fromStorage("xx")).isEqualTo(AppLocale.SYSTEM)
    }

    @Test
    fun isRtl_arabicTrueEnglishFalse() {
        assertThat(LocaleController.isRtl(AppLocale.ARABIC)).isTrue()
        assertThat(LocaleController.isRtl(AppLocale.ENGLISH)).isFalse()
    }
}

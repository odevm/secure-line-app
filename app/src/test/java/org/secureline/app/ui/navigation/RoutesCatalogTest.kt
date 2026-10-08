package org.secureline.app.ui.navigation

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class RoutesCatalogTest {
    @Test
    fun allRoutes_areUniqueAndCoverShellScreens() {
        assertThat(Routes.ALL).containsNoDuplicates()
        assertThat(Routes.ALL).containsAtLeast(
            Routes.ONBOARDING,
            Routes.UNLOCK,
            Routes.HOME,
            Routes.PAIR,
            Routes.SAS,
            Routes.SECURE_CALL,
            Routes.SMS_CHAT,
            Routes.SETTINGS,
            Routes.SESSION_ERROR,
        )
        assertThat(Routes.ALL).hasSize(17)
    }
}

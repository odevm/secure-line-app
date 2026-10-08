package org.secureline.app.domain.account

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class AccountControllerTest {
    @Test
    fun offlineSkipGoogle_stillEnablesSecureFeatures() {
        val c = AccountController("local-1")
        c.skipGoogle()
        assertThat(c.state.canUseSecureFeatures).isTrue()
        assertThat(c.state.canUseDrive).isFalse()
        assertThat(c.state.googleLinked).isFalse()
    }

    @Test
    fun linkGoogle_enablesDrive_fcmWorksWithoutGoogle() {
        val c = AccountController("local-1")
        c.onNetworkAvailable(fcmTokenPresent = true)
        assertThat(c.state.fcmReady).isTrue()
        assertThat(c.state.googleLinked).isFalse()
        c.linkGoogle("fid-1", "a@b.c")
        assertThat(c.state.canUseDrive).isTrue()
        c.signOutGoogle()
        assertThat(c.state.canUseDrive).isFalse()
        assertThat(c.state.fcmReady).isTrue()
    }
}

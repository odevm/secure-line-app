package org.secureline.app.domain.session

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class SecureSessionControllerTest {
    @Test
    fun tofuPath_syncHandshakeSasThenPtt() {
        val c = SecureSessionController()
        c.enterSecureMode()
        assertThat(c.state).isEqualTo(SecureSessionState.ModemSync)
        c.onSyncSuccess(needsHandshake = true)
        c.onHandshakeOk(alreadyVerified = false)
        assertThat(c.state).isEqualTo(SecureSessionState.SasGate)
        c.onSasMatches()
        assertThat(c.state).isEqualTo(SecureSessionState.PttReady)
    }

    @Test
    fun failureStates() {
        val c = SecureSessionController()
        c.enterSecureMode()
        c.onSyncTimeout()
        assertThat(c.state).isEqualTo(SecureSessionState.SyncTimeout)
        c.enterSecureMode()
        c.onPeerNoApp()
        assertThat(c.state).isEqualTo(SecureSessionState.PeerNoApp)
        c.enterSecureMode()
        c.onSyncSuccess(true)
        c.onHandshakeFail()
        assertThat(c.state).isEqualTo(SecureSessionState.HandshakeFail)
        c.onHandshakeOk(false)
        c.onSasMismatch()
        assertThat(c.state).isEqualTo(SecureSessionState.SasMismatch)
    }
}

package org.secureline.app.domain.session

/**
 * Secure-call session machine (TOFU + SAS gate before PTT).
 */
enum class SecureSessionState {
    Idle,
    ModemSync,
    Handshake,
    SasGate,
    PttReady,
    SyncTimeout,
    PeerNoApp,
    HandshakeFail,
    SasMismatch,
    KeyChanged,
}

class SecureSessionController {
    var state: SecureSessionState = SecureSessionState.Idle
        private set

    fun enterSecureMode() {
        state = SecureSessionState.ModemSync
    }

    fun onSyncSuccess(needsHandshake: Boolean) {
        state = if (needsHandshake) SecureSessionState.Handshake else SecureSessionState.SasGate
    }

    fun onSyncTimeout() {
        state = SecureSessionState.SyncTimeout
    }

    fun onPeerNoApp() {
        state = SecureSessionState.PeerNoApp
    }

    fun onHandshakeOk(alreadyVerified: Boolean) {
        state = if (alreadyVerified) SecureSessionState.PttReady else SecureSessionState.SasGate
    }

    fun onHandshakeFail() {
        state = SecureSessionState.HandshakeFail
    }

    fun onSasMatches() {
        state = SecureSessionState.PttReady
    }

    fun onSasMismatch() {
        state = SecureSessionState.SasMismatch
    }

    fun onKeyChanged() {
        state = SecureSessionState.KeyChanged
    }

    fun abortToIdle() {
        state = SecureSessionState.Idle
    }
}

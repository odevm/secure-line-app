package org.secureline.app.domain.root

enum class RootStatus {
    NotRooted,
    Available,
    Granted,
}

/**
 * Optional root audio path. Non-root path must remain unchanged when root is denied.
 */
class RootAudioGate {
    var status: RootStatus = RootStatus.NotRooted
        private set
    var userOptIn: Boolean = false
        private set

    fun detect(suPresent: Boolean) {
        status = if (suPresent) RootStatus.Available else RootStatus.NotRooted
        if (!suPresent) {
            userOptIn = false
        }
    }

    fun requestGrant(userConfirmed: Boolean): Boolean {
        if (status == RootStatus.NotRooted) return false
        if (!userConfirmed) return false
        userOptIn = true
        status = RootStatus.Granted
        return true
    }

    fun denyOrRevoke() {
        userOptIn = false
        if (status == RootStatus.Granted) {
            status = RootStatus.Available
        }
    }

    /** True only when explicitly granted; otherwise callers must use non-root audio. */
    fun useRootAudioPath(): Boolean = userOptIn && status == RootStatus.Granted
}

package org.secureline.app.domain.account

/**
 * Offline-first account: Google is optional and only enables Drive.
 * FCM / Analytics / Crashlytics may run with network without Google.
 */
data class AccountState(
    val localUserId: String,
    val googleLinked: Boolean = false,
    val firebaseUid: String? = null,
    val email: String? = null,
    val driveBackupEnabled: Boolean = false,
    val fcmReady: Boolean = false,
    val analyticsConsent: Boolean = false,
    val crashlyticsConsent: Boolean = false,
) {
    val canUseSecureFeatures: Boolean get() = localUserId.isNotBlank()
    val canUseDrive: Boolean get() = googleLinked && driveBackupEnabled
}

class AccountController(
    initialLocalUserId: String,
) {
    var state: AccountState = AccountState(localUserId = initialLocalUserId)
        private set

    fun skipGoogle() {
        // Onboarding continues offline; Drive stays gated.
        state = state.copy(googleLinked = false, driveBackupEnabled = false)
    }

    fun linkGoogle(
        firebaseUid: String,
        email: String,
    ) {
        require(firebaseUid.isNotBlank())
        state =
            state.copy(
                googleLinked = true,
                firebaseUid = firebaseUid,
                email = email,
                driveBackupEnabled = true,
            )
    }

    fun signOutGoogle() {
        state =
            state.copy(
                googleLinked = false,
                firebaseUid = null,
                email = null,
                driveBackupEnabled = false,
            )
    }

    fun onNetworkAvailable(fcmTokenPresent: Boolean) {
        state = state.copy(fcmReady = fcmTokenPresent)
    }

    fun setAnalyticsConsent(enabled: Boolean) {
        state = state.copy(analyticsConsent = enabled)
    }

    fun setCrashlyticsConsent(enabled: Boolean) {
        state = state.copy(crashlyticsConsent = enabled)
    }
}

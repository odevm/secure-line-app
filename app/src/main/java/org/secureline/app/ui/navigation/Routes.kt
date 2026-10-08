package org.secureline.app.ui.navigation

/**
 * All first-class navigation destinations for SecureLine shell stubs.
 * Keep [ALL] in sync when adding screens so instrumented smoke stays complete.
 */
object Routes {
    const val ONBOARDING = "onboarding"
    const val UNLOCK = "unlock"
    const val HOME = "home"
    const val PAIR = "pair"
    const val CONTACT_DETAIL = "contact_detail"
    const val CELLULAR_CALL = "cellular_call"
    const val SAS = "sas"
    const val SECURE_CALL = "secure_call"
    const val SMS_CHAT = "sms_chat"
    const val CALIBRATION = "calibration"
    const val SETTINGS = "settings"
    const val ACCOUNT = "account"
    const val BACKUP = "backup"
    const val SUPPORT_CONTACT = "support_contact"
    const val UPDATE_RESULT = "update_result"
    const val PERMISSION_BLOCKED = "permission_blocked"
    const val SESSION_ERROR = "session_error"

    val ALL: List<String> =
        listOf(
            ONBOARDING,
            UNLOCK,
            HOME,
            PAIR,
            CONTACT_DETAIL,
            CELLULAR_CALL,
            SAS,
            SECURE_CALL,
            SMS_CHAT,
            CALIBRATION,
            SETTINGS,
            ACCOUNT,
            BACKUP,
            SUPPORT_CONTACT,
            UPDATE_RESULT,
            PERMISSION_BLOCKED,
            SESSION_ERROR,
        )
}

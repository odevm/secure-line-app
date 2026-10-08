package org.secureline.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.secureline.app.domain.account.AccountState
import org.secureline.app.domain.contacts.Contact
import org.secureline.app.domain.sas.SasVerifier
import org.secureline.app.domain.session.SecureSessionState
import org.secureline.app.ui.account.AccountScreen
import org.secureline.app.ui.call.SecureCallScreen
import org.secureline.app.ui.common.StubScreen
import org.secureline.app.ui.home.HomeScreen
import org.secureline.app.ui.i18n.AppLocale
import org.secureline.app.ui.sas.SasScreen
import org.secureline.app.ui.settings.SettingsScreen
import org.secureline.app.ui.sms.SmsChatScreen
import org.secureline.app.ui.support.SupportContactScreen
import org.secureline.app.ui.unlock.UnlockScreen

@Composable
fun SecureLineNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.HOME,
    appLocale: AppLocale = AppLocale.SYSTEM,
    onLocaleSelected: (AppLocale) -> Unit = {},
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(Routes.ONBOARDING) {
            StubScreen(title = "Onboarding", routeTag = Routes.ONBOARDING)
        }
        composable(Routes.UNLOCK) {
            UnlockScreen(errorMessage = null, onUnlock = {})
        }
        composable(Routes.HOME) {
            HomeScreen()
        }
        composable(Routes.PAIR) {
            StubScreen(title = "Pair contact", routeTag = Routes.PAIR)
        }
        composable(Routes.CONTACT_DETAIL) {
            StubScreen(title = "Contact detail", routeTag = Routes.CONTACT_DETAIL)
        }
        composable(Routes.CELLULAR_CALL) {
            StubScreen(title = "Cellular call", routeTag = Routes.CELLULAR_CALL)
        }
        composable(Routes.SAS) {
            SasScreen(
                material = SasVerifier.derive(ByteArray(32) { 0 }),
                onMatches = {},
                onMismatch = {},
            )
        }
        composable(Routes.SECURE_CALL) {
            SecureCallScreen(
                sessionState = SecureSessionState.PttReady,
                onPttPressedChange = {},
            )
        }
        composable(Routes.SMS_CHAT) {
            val demo =
                Contact(
                    peerLocalUserId = "00000000-0000-0000-0000-000000000001",
                    x25519PublicKey = ByteArray(32),
                    ed25519PublicKey = ByteArray(32),
                    keyFingerprint = "DEMO",
                    phoneNumbers = listOf("+15550001", "+15550002"),
                )
            var selected by remember { mutableStateOf(demo.phoneNumbers.first()) }
            SmsChatScreen(
                contact = demo,
                liveFingerprint = "DEMO",
                phoneOptions = demo.phoneNumbers,
                selectedPhone = selected,
                onSelectPhone = { selected = it },
                onSend = {},
            )
        }
        composable(Routes.CALIBRATION) {
            StubScreen(title = "Calibration", routeTag = Routes.CALIBRATION)
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(
                currentLocale = appLocale,
                onLocaleSelected = onLocaleSelected,
            )
        }
        composable(Routes.ACCOUNT) {
            var account by remember {
                mutableStateOf(AccountState(localUserId = "local-preview"))
            }
            AccountScreen(
                state = account,
                onSkipGoogle = {
                    account = account.copy(googleLinked = false, driveBackupEnabled = false)
                },
                onLinkGoogle = {
                    account =
                        account.copy(
                            googleLinked = true,
                            firebaseUid = "preview-fid",
                            email = "user@example.com",
                            driveBackupEnabled = true,
                        )
                },
                onSignOutGoogle = {
                    account =
                        account.copy(
                            googleLinked = false,
                            firebaseUid = null,
                            email = null,
                            driveBackupEnabled = false,
                        )
                },
            )
        }
        composable(Routes.BACKUP) {
            StubScreen(title = "Backup & restore", routeTag = Routes.BACKUP)
        }
        composable(Routes.SUPPORT_CONTACT) {
            SupportContactScreen()
        }
        composable(Routes.UPDATE_RESULT) {
            StubScreen(title = "Update result", routeTag = Routes.UPDATE_RESULT)
        }
        composable(Routes.PERMISSION_BLOCKED) {
            StubScreen(title = "Permission blocked", routeTag = Routes.PERMISSION_BLOCKED)
        }
        composable(Routes.SESSION_ERROR) {
            StubScreen(title = "Session error", routeTag = Routes.SESSION_ERROR)
        }
    }
}

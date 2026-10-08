package org.secureline.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.secureline.app.domain.sas.SasVerifier
import org.secureline.app.ui.common.StubScreen
import org.secureline.app.ui.home.HomeScreen
import org.secureline.app.ui.i18n.AppLocale
import org.secureline.app.ui.sas.SasScreen
import org.secureline.app.ui.settings.SettingsScreen
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
            UnlockScreen(
                errorMessage = null,
                onUnlock = {},
            )
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
            StubScreen(title = "Secure Call", routeTag = Routes.SECURE_CALL)
        }
        composable(Routes.SMS_CHAT) {
            StubScreen(title = "Secure SMS", routeTag = Routes.SMS_CHAT)
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
            StubScreen(title = "Account", routeTag = Routes.ACCOUNT)
        }
        composable(Routes.BACKUP) {
            StubScreen(title = "Backup & restore", routeTag = Routes.BACKUP)
        }
        composable(Routes.SUPPORT_CONTACT) {
            StubScreen(title = "Contact", routeTag = Routes.SUPPORT_CONTACT)
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

package org.secureline.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.secureline.app.ui.common.StubScreen
import org.secureline.app.ui.home.HomeScreen

@Composable
fun SecureLineNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.HOME,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(Routes.ONBOARDING) {
            StubScreen(title = "Onboarding", routeTag = Routes.ONBOARDING)
        }
        composable(Routes.UNLOCK) {
            StubScreen(title = "Unlock", routeTag = Routes.UNLOCK)
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
            StubScreen(title = "SAS compare", routeTag = Routes.SAS)
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
            StubScreen(title = "Settings", routeTag = Routes.SETTINGS)
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

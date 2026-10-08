package org.secureline.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.secureline.app.ui.MainActivity
import org.secureline.app.ui.i18n.AppLocale
import org.secureline.app.ui.navigation.Routes
import org.secureline.app.ui.navigation.SecureLineNavHost
import org.secureline.app.ui.theme.SecureLineTheme

@HiltAndroidTest
class LocaleSwapSmokeTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun settings_showsLocaleButtons() {
        composeRule.setContent {
            SecureLineTheme {
                SecureLineNavHost(
                    startDestination = Routes.SETTINGS,
                    appLocale = AppLocale.ENGLISH,
                    onLocaleSelected = {},
                )
            }
        }
        composeRule.onNodeWithTag("locale_en").assertIsDisplayed()
        composeRule.onNodeWithTag("locale_ar").assertIsDisplayed()
        composeRule.onNodeWithTag("locale_ar").performClick()
    }
}

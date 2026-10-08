package org.secureline.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.secureline.app.domain.contacts.Contact
import org.secureline.app.ui.MainActivity
import org.secureline.app.ui.sms.SmsChatScreen
import org.secureline.app.ui.theme.SecureLineTheme

@HiltAndroidTest
class SmsMismatchSmokeTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun mismatch_showsBlockedBanner() {
        val contact =
            Contact(
                peerLocalUserId = "00000000-0000-0000-0000-000000000001",
                x25519PublicKey = ByteArray(32),
                ed25519PublicKey = ByteArray(32),
                keyFingerprint = "AAAA",
                phoneNumbers = listOf("+1555"),
            )
        composeRule.setContent {
            SecureLineTheme {
                SmsChatScreen(
                    contact = contact,
                    liveFingerprint = "BBBB",
                    phoneOptions = contact.phoneNumbers,
                    selectedPhone = "+1555",
                    onSelectPhone = {},
                    onSend = {},
                )
            }
        }
        composeRule.onNodeWithTag("sms_blocked").assertIsDisplayed()
    }
}

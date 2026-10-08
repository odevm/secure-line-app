package org.secureline.app.config

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class AppConfigTest {
    @Test
    fun blankUrls_disableContactAndUpdates() {
        val config =
            AppConfig(
                githubRepoUrl = "",
                githubOwner = "",
                githubRepo = "",
                telegramChannelUrl = "",
                firebaseEnabled = false,
            )
        assertThat(config.hasGithubContact).isFalse()
        assertThat(config.hasTelegramContact).isFalse()
        assertThat(config.canCheckUpdates).isFalse()
    }

    @Test
    fun populatedUrls_enableContactAndUpdates() {
        val config =
            AppConfig(
                githubRepoUrl = "https://github.com/org/secureline",
                githubOwner = "org",
                githubRepo = "secureline",
                telegramChannelUrl = "https://t.me/secureline",
                firebaseEnabled = true,
            )
        assertThat(config.hasGithubContact).isTrue()
        assertThat(config.hasTelegramContact).isTrue()
        assertThat(config.canCheckUpdates).isTrue()
        assertThat(config.firebaseEnabled).isTrue()
    }
}

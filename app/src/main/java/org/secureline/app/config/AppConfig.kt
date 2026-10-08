package org.secureline.app.config

import org.secureline.app.BuildConfig

/**
 * Typed view of public build-time SecureLine configuration.
 * Blank URLs disable Contact rows and update checks.
 */
data class AppConfig(
    val githubRepoUrl: String,
    val githubOwner: String,
    val githubRepo: String,
    val telegramChannelUrl: String,
    val firebaseEnabled: Boolean,
) {
    val hasGithubContact: Boolean get() = githubRepoUrl.isNotBlank()
    val hasTelegramContact: Boolean get() = telegramChannelUrl.isNotBlank()
    val canCheckUpdates: Boolean get() = githubOwner.isNotBlank() && githubRepo.isNotBlank()

    companion object {
        fun fromBuildConfig(): AppConfig =
            AppConfig(
                githubRepoUrl = BuildConfig.GITHUB_REPO_URL,
                githubOwner = BuildConfig.GITHUB_OWNER,
                githubRepo = BuildConfig.GITHUB_REPO,
                telegramChannelUrl = BuildConfig.TELEGRAM_CHANNEL_URL,
                firebaseEnabled = BuildConfig.FIREBASE_ENABLED,
            )
    }
}

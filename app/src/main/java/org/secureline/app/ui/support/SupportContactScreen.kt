package org.secureline.app.ui.support

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.secureline.app.BuildConfig
import org.secureline.app.R
import org.secureline.app.config.AppConfig
import org.secureline.app.ui.navigation.Routes

@Composable
fun SupportContactScreen(
    config: AppConfig = AppConfig.fromBuildConfig(),
) {
    val context = LocalContext.current
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp)
                .testTag("screen_${Routes.SUPPORT_CONTACT}"),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.support_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = stringResource(R.string.support_version, BuildConfig.VERSION_NAME),
            style = MaterialTheme.typography.bodyMedium,
        )
        ContactRow(
            label = stringResource(R.string.contact_github),
            enabled = config.hasGithubContact,
            testTag = "contact_github",
            onClick = {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(config.githubRepoUrl)),
                )
            },
        )
        ContactRow(
            label = stringResource(R.string.contact_telegram),
            enabled = config.hasTelegramContact,
            testTag = "contact_telegram",
            onClick = {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(config.telegramChannelUrl)),
                )
            },
        )
        if (!config.hasTelegramContact) {
            Text(
                text = stringResource(R.string.not_configured),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.testTag("contact_telegram_missing"),
            )
        }
    }
}

@Composable
private fun ContactRow(
    label: String,
    enabled: Boolean,
    testTag: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier =
            Modifier
                .fillMaxWidth()
                .testTag(testTag),
    ) {
        Text(label)
    }
}

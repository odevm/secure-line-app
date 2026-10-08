package org.secureline.app.ui.settings

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.secureline.app.R
import org.secureline.app.ui.i18n.AppLocale
import org.secureline.app.ui.navigation.Routes

@Composable
fun SettingsScreen(
    currentLocale: AppLocale,
    onLocaleSelected: (AppLocale) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp)
                .testTag("screen_${Routes.SETTINGS}"),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.settings_language),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        LocaleButton(
            label = stringResource(R.string.language_system),
            selected = currentLocale == AppLocale.SYSTEM,
            onClick = { onLocaleSelected(AppLocale.SYSTEM) },
            tag = "locale_system",
        )
        LocaleButton(
            label = stringResource(R.string.language_english),
            selected = currentLocale == AppLocale.ENGLISH,
            onClick = { onLocaleSelected(AppLocale.ENGLISH) },
            tag = "locale_en",
        )
        LocaleButton(
            label = stringResource(R.string.language_arabic),
            selected = currentLocale == AppLocale.ARABIC,
            onClick = { onLocaleSelected(AppLocale.ARABIC) },
            tag = "locale_ar",
        )
    }
}

@Composable
private fun LocaleButton(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    tag: String,
) {
    Button(
        onClick = onClick,
        modifier =
            Modifier
                .fillMaxWidth()
                .testTag(tag),
        enabled = !selected,
    ) {
        Text(text = label)
    }
}

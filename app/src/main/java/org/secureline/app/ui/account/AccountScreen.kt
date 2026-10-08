package org.secureline.app.ui.account

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
import org.secureline.app.domain.account.AccountState
import org.secureline.app.ui.navigation.Routes

@Composable
fun AccountScreen(
    state: AccountState,
    onSkipGoogle: () -> Unit,
    onLinkGoogle: () -> Unit,
    onSignOutGoogle: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp)
                .testTag("screen_${Routes.ACCOUNT}"),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(stringResource(R.string.account_title), style = MaterialTheme.typography.headlineMedium)
        Text(stringResource(R.string.account_local_id, state.localUserId))
        Text(
            text =
                if (state.googleLinked) {
                    stringResource(R.string.account_google_linked, state.email.orEmpty())
                } else {
                    stringResource(R.string.account_google_optional)
                },
            modifier = Modifier.testTag("account_google_status"),
        )
        Text(
            text =
                if (state.canUseDrive) {
                    stringResource(R.string.account_drive_on)
                } else {
                    stringResource(R.string.account_drive_off)
                },
        )
        if (!state.googleLinked) {
            Button(
                onClick = onSkipGoogle,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .testTag("account_skip_google"),
            ) {
                Text(stringResource(R.string.account_skip_google))
            }
            Button(
                onClick = onLinkGoogle,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .testTag("account_link_google"),
            ) {
                Text(stringResource(R.string.account_link_google))
            }
        } else {
            Button(
                onClick = onSignOutGoogle,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .testTag("account_signout_google"),
            ) {
                Text(stringResource(R.string.account_signout_google))
            }
        }
    }
}

package org.secureline.app.ui.call

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.secureline.app.R
import org.secureline.app.domain.session.SecureSessionState
import org.secureline.app.ui.navigation.Routes

@Composable
fun SecureCallScreen(
    sessionState: SecureSessionState,
    onPttPressedChange: (Boolean) -> Unit,
) {
    var pressing by remember { mutableStateOf(false) }
    val ready = sessionState == SecureSessionState.PttReady
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp)
                .testTag("screen_${Routes.SECURE_CALL}"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.secure_call_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = stringResource(R.string.secure_call_state, sessionState.name),
            modifier = Modifier.testTag("secure_call_state"),
        )
        Button(
            onClick = {
                pressing = !pressing
                onPttPressedChange(pressing)
            },
            enabled = ready,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .testTag("ptt_button"),
        ) {
            Text(
                if (pressing) {
                    stringResource(R.string.ptt_release)
                } else {
                    stringResource(R.string.ptt_press)
                },
            )
        }
    }
}

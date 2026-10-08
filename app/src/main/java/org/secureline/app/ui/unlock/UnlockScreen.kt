package org.secureline.app.ui.unlock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import org.secureline.app.R
import org.secureline.app.ui.navigation.Routes

@Composable
fun UnlockScreen(
    errorMessage: String?,
    onUnlock: (String) -> Unit,
) {
    var pin by remember { mutableStateOf("") }
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp)
                .testTag("screen_${Routes.UNLOCK}"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.unlock_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        OutlinedTextField(
            value = pin,
            onValueChange = { if (it.length <= 12 && it.all(Char::isDigit)) pin = it },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .testTag("unlock_pin"),
            label = { Text(stringResource(R.string.unlock_pin_label)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            singleLine = true,
        )
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.testTag("unlock_error"),
            )
        }
        Button(
            onClick = { onUnlock(pin) },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .testTag("unlock_submit"),
            enabled = pin.length >= 6,
        ) {
            Text(stringResource(R.string.unlock_submit))
        }
    }
}

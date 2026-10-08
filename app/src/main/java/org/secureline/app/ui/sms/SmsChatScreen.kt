package org.secureline.app.ui.sms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import org.secureline.app.R
import org.secureline.app.domain.contacts.Contact
import org.secureline.app.domain.sms.SmsSendDecision
import org.secureline.app.domain.sms.SmsSendGate
import org.secureline.app.ui.navigation.Routes

@Composable
fun SmsChatScreen(
    contact: Contact?,
    liveFingerprint: String,
    phoneOptions: List<String>,
    selectedPhone: String?,
    onSelectPhone: (String) -> Unit,
    onSend: (String) -> Unit,
) {
    var draft by remember { mutableStateOf("") }
    val decision = SmsSendGate.decide(contact, liveFingerprint)
    val blocked = decision is SmsSendDecision.Block

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp)
                .testTag("screen_${Routes.SMS_CHAT}"),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.sms_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        if (phoneOptions.size > 1) {
            Text(stringResource(R.string.sms_pick_number))
            phoneOptions.forEach { number ->
                Button(
                    onClick = { onSelectPhone(number) },
                    modifier = Modifier.testTag("sms_phone_$number"),
                    enabled = selectedPhone != number,
                ) {
                    Text(number)
                }
            }
        }
        if (blocked) {
            Text(
                text = stringResource(R.string.sms_blocked_mismatch),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.testTag("sms_blocked"),
            )
        }
        OutlinedTextField(
            value = draft,
            onValueChange = { draft = it },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .testTag("sms_composer"),
            enabled = !blocked,
        )
        Button(
            onClick = {
                onSend(draft)
                draft = ""
            },
            modifier = Modifier.testTag("sms_send"),
            enabled = !blocked && draft.isNotBlank() && !selectedPhone.isNullOrBlank(),
        ) {
            Text(stringResource(R.string.sms_send))
        }
    }
}

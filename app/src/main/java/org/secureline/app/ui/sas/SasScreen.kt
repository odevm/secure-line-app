package org.secureline.app.ui.sas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.secureline.app.R
import org.secureline.app.domain.sas.SasMaterial
import org.secureline.app.ui.navigation.Routes

@Composable
fun SasScreen(
    material: SasMaterial,
    onMatches: () -> Unit,
    onMismatch: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp)
                .testTag("screen_${Routes.SAS}"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.sas_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = stringResource(R.string.sas_instructions),
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = material.emojiCodes().joinToString(" "),
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.testTag("sas_emoji"),
        )
        Text(
            text = material.digitGroups.joinToString(" "),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.testTag("sas_digits"),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Button(
                onClick = onMatches,
                modifier =
                    Modifier
                        .weight(1f)
                        .testTag("sas_matches"),
            ) {
                Text(stringResource(R.string.sas_matches))
            }
            OutlinedButton(
                onClick = onMismatch,
                modifier =
                    Modifier
                        .weight(1f)
                        .testTag("sas_mismatch"),
            ) {
                Text(stringResource(R.string.sas_mismatch))
            }
        }
    }
}

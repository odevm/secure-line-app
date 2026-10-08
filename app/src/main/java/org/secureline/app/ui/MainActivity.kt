package org.secureline.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import org.secureline.app.ui.navigation.SecureLineNavHost
import org.secureline.app.ui.theme.SecureLineTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SecureLineTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SecureLineNavHost()
                }
            }
        }
    }
}

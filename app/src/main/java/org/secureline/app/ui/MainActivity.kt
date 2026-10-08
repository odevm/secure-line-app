package org.secureline.app.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import dagger.hilt.android.AndroidEntryPoint
import org.secureline.app.ui.i18n.LocaleController
import org.secureline.app.ui.i18n.LocalePreferences
import org.secureline.app.ui.navigation.SecureLineNavHost
import org.secureline.app.ui.theme.SecureLineTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var localePreferences: LocalePreferences

    override fun attachBaseContext(newBase: Context) {
        val prefs = LocalePreferences(newBase)
        localePreferences = prefs
        super.attachBaseContext(LocaleController.apply(newBase, prefs.get()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var appLocale by remember { mutableStateOf(localePreferences.get()) }
            val layoutDirection =
                if (LocaleController.isRtl(appLocale)) {
                    LayoutDirection.Rtl
                } else {
                    LayoutDirection.Ltr
                }
            CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                SecureLineTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        SecureLineNavHost(
                            appLocale = appLocale,
                            onLocaleSelected = { selected ->
                                localePreferences.set(selected)
                                appLocale = selected
                                recreate()
                            },
                        )
                    }
                }
            }
        }
    }
}

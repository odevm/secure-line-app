package org.secureline.app.ui.i18n

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleController {
    fun apply(
        context: Context,
        appLocale: AppLocale,
    ): Context {
        if (appLocale == AppLocale.SYSTEM) {
            return context
        }
        val locale = Locale.forLanguageTag(appLocale.storageValue)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }

    fun isRtl(appLocale: AppLocale): Boolean =
        when (appLocale) {
            AppLocale.ARABIC -> true
            AppLocale.ENGLISH -> false
            AppLocale.SYSTEM ->
                Locale.getDefault().language.equals("ar", ignoreCase = true)
        }
}

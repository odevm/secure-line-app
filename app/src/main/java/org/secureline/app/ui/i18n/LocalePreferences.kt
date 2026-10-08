package org.secureline.app.ui.i18n

import android.content.Context

class LocalePreferences(
    context: Context,
) {
    private val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun get(): AppLocale = AppLocale.fromStorage(prefs.getString(KEY, AppLocale.SYSTEM.storageValue))

    fun set(locale: AppLocale) {
        prefs.edit().putString(KEY, locale.storageValue).apply()
    }

    companion object {
        private const val PREFS = "secureline_locale"
        private const val KEY = "app_locale"
    }
}

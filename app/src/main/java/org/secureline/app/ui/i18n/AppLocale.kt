package org.secureline.app.ui.i18n

enum class AppLocale(
    val storageValue: String,
) {
    SYSTEM("system"),
    ENGLISH("en"),
    ARABIC("ar"),
    ;

    companion object {
        fun fromStorage(value: String?): AppLocale =
            entries.firstOrNull { it.storageValue == value } ?: SYSTEM
    }
}

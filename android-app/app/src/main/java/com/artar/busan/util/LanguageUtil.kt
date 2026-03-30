package com.artar.busan.util

import java.util.Locale

object LanguageUtil {
    fun resolveText(i18n: Map<String, String>, language: String): String {
        return i18n[language]
            ?: i18n[language.lowercase(Locale.getDefault())]
            ?: i18n["en"]
            ?: i18n.values.firstOrNull()
            ?: ""
    }
}

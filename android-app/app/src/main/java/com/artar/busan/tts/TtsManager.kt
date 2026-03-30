package com.artar.busan.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class TtsManager(context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var initialized = false

    override fun onInit(status: Int) {
        initialized = status == TextToSpeech.SUCCESS
    }

    fun speak(text: String, language: String): Boolean {
        val engine = tts ?: return false
        if (!initialized) return false

        val locale = when (language.lowercase()) {
            "ko" -> Locale.KOREAN
            "jp", "ja" -> Locale.JAPANESE
            "cn", "zh" -> Locale.SIMPLIFIED_CHINESE
            else -> Locale.ENGLISH
        }
        val result = engine.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            return false
        }
        engine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "artar-tts")
        return true
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}

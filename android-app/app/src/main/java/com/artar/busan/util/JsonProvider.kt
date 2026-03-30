package com.artar.busan.util

import kotlinx.serialization.json.Json

object JsonProvider {
    val instance: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }
}

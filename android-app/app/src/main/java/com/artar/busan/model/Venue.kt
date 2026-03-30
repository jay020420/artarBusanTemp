package com.artar.busan.model

import kotlinx.serialization.Serializable

@Serializable
data class Venue(
    val id: String,
    val eventId: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val descriptionI18n: Map<String, String>
)

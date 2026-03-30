package com.artar.busan.model

import kotlinx.serialization.Serializable

@Serializable
data class Artwork(
    val id: String,
    val venueId: String,
    val markerImageKey: String,
    val titleI18n: Map<String, String>,
    val artist: String,
    val descriptionI18n: Map<String, String>,
    val thumbnailUrl: String,
    val audioGuideAvailable: Boolean
)

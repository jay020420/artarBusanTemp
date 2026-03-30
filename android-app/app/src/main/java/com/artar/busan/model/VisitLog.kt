package com.artar.busan.model

import kotlinx.serialization.Serializable

@Serializable
data class VisitLog(
    val artworkId: String? = null,
    val venueId: String? = null,
    val visitedAt: Long,
    val language: String,
    val storedLocally: Boolean = true
)

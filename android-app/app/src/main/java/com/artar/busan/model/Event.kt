package com.artar.busan.model

import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val id: String,
    val name: String,
    val slug: String,
    val primaryColor: String,
    val secondaryColor: String,
    val logoUrl: String
)

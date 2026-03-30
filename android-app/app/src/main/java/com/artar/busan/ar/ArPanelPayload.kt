package com.artar.busan.ar

import com.artar.busan.model.Artwork

data class ArPanelPayload(
    val artwork: Artwork,
    val language: String
)

package com.artar.busan.data

import com.artar.busan.model.Artwork
import com.artar.busan.model.Event

interface ArtArRepository {
    suspend fun getEvents(): List<Event>
    suspend fun getArtworksByEvent(eventId: String): List<Artwork>
    suspend fun getArtwork(artworkId: String): Artwork?
}

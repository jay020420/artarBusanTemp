package com.artar.busan.data

import com.artar.busan.model.Artwork
import com.artar.busan.model.Event

class ApiArtArDataSource(
    private val apiService: ArtArApiService
) : ArtArDataSource {
    override suspend fun getEvents(): List<Event> = apiService.getEvents()

    override suspend fun getArtworksByEvent(eventId: String): List<Artwork> =
        apiService.getArtworksByEvent(eventId)

    override suspend fun getArtwork(artworkId: String): Artwork? =
        runCatching { apiService.getArtwork(artworkId) }.getOrNull()
}

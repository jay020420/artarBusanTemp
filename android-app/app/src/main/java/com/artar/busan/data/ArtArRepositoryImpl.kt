package com.artar.busan.data

import com.artar.busan.model.Artwork
import com.artar.busan.model.Event

class ArtArRepositoryImpl(
    private val apiDataSource: ArtArDataSource,
    private val mockDataSource: ArtArDataSource
) : ArtArRepository {

    override suspend fun getEvents(): List<Event> {
        return runCatching { apiDataSource.getEvents() }
            .getOrElse { mockDataSource.getEvents() }
    }

    override suspend fun getArtworksByEvent(eventId: String): List<Artwork> {
        return runCatching { apiDataSource.getArtworksByEvent(eventId) }
            .getOrElse { mockDataSource.getArtworksByEvent(eventId) }
    }

    override suspend fun getArtwork(artworkId: String): Artwork? {
        return runCatching { apiDataSource.getArtwork(artworkId) }
            .getOrElse { mockDataSource.getArtwork(artworkId) }
    }
}

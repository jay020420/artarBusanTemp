package com.artar.busan.domain

import com.artar.busan.data.ArtArRepository
import com.artar.busan.model.Artwork
import com.artar.busan.model.Event

class GetEventsUseCase(private val repository: ArtArRepository) {
    suspend operator fun invoke(): List<Event> = repository.getEvents()
}

class GetArtworksByEventUseCase(private val repository: ArtArRepository) {
    suspend operator fun invoke(eventId: String): List<Artwork> = repository.getArtworksByEvent(eventId)
}

class GetArtworkUseCase(private val repository: ArtArRepository) {
    suspend operator fun invoke(artworkId: String): Artwork? = repository.getArtwork(artworkId)
}

package com.artar.busan.data

import com.artar.busan.model.Artwork
import com.artar.busan.model.Event
import retrofit2.http.GET
import retrofit2.http.Path

interface ArtArApiService {
    @GET("events")
    suspend fun getEvents(): List<Event>

    @GET("events/{eventId}/artworks")
    suspend fun getArtworksByEvent(@Path("eventId") eventId: String): List<Artwork>

    @GET("artworks/{artworkId}")
    suspend fun getArtwork(@Path("artworkId") artworkId: String): Artwork
}

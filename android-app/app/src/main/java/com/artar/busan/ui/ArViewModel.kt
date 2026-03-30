package com.artar.busan.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artar.busan.checkin.StampStore
import com.artar.busan.data.ArtArRepository
import com.artar.busan.model.Artwork
import com.artar.busan.model.VisitLog
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ArViewModel(
    private val repository: ArtArRepository,
    private val stampStore: StampStore
) : ViewModel() {

    private val _artworks = MutableLiveData<List<Artwork>>(emptyList())
    val artworks: LiveData<List<Artwork>> = _artworks

    private val _currentLanguage = MutableLiveData("ko")
    val currentLanguage: LiveData<String> = _currentLanguage

    private val _lastCheckin = MutableLiveData<Boolean?>(null)
    val lastCheckin: LiveData<Boolean?> = _lastCheckin

    fun loadArtworks(eventId: String) {
        viewModelScope.launch {
            _artworks.value = repository.getArtworksByEvent(eventId)
        }
    }

    fun setLanguage(language: String) {
        _currentLanguage.value = language
    }

    fun findArtworkByMarker(markerKey: String): Artwork? {
        return _artworks.value?.firstOrNull { it.markerImageKey == markerKey }
    }

    fun checkIn(artworkId: String) {
        viewModelScope.launch {
            val inserted = stampStore.saveVisit(
                VisitLog(
                    artworkId = artworkId,
                    visitedAt = System.currentTimeMillis(),
                    language = _currentLanguage.value ?: "ko",
                    storedLocally = true
                )
            )
            _lastCheckin.value = inserted
        }
    }

    suspend fun isCheckedIn(artworkId: String): Boolean {
        return stampStore.observedStampIds().first().contains(artworkId)
    }
}

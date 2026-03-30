package com.artar.busan.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artar.busan.data.ArtArRepository
import com.artar.busan.model.Event
import kotlinx.coroutines.launch

class EventSelectionViewModel(
    private val repository: ArtArRepository
) : ViewModel() {

    private val _events = MutableLiveData<List<Event>>(emptyList())
    val events: LiveData<List<Event>> = _events

    fun loadEvents() {
        viewModelScope.launch {
            _events.value = repository.getEvents()
        }
    }
}

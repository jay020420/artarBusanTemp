package com.artar.busan.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artar.busan.checkin.StampStore
import com.artar.busan.data.ApiArtArDataSource
import com.artar.busan.data.ArtArRepository
import com.artar.busan.data.ArtArRepositoryImpl
import com.artar.busan.data.MockArtArDataSource
import com.artar.busan.data.NetworkModule

class AppViewModelFactory(
    private val stampStore: StampStore
) : ViewModelProvider.Factory {

    private fun createRepository(): ArtArRepository {
        val mock = MockArtArDataSource()
        val api = ApiArtArDataSource(NetworkModule.apiService)
        return ArtArRepositoryImpl(apiDataSource = api, mockDataSource = mock)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = createRepository()
        return when {
            modelClass.isAssignableFrom(EventSelectionViewModel::class.java) -> {
                EventSelectionViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ArViewModel::class.java) -> {
                ArViewModel(repository, stampStore) as T
            }
            else -> error("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

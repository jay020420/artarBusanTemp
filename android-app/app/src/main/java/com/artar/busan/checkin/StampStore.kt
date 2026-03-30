package com.artar.busan.checkin

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.artar.busan.model.VisitLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "stamp_store")

class StampStore(private val context: Context) {
    private val stampsKey = stringSetPreferencesKey("stamps")

    fun observedStampIds(): Flow<Set<String>> = context.dataStore.data.map { pref ->
        pref[stampsKey] ?: emptySet()
    }

    suspend fun saveVisit(visitLog: VisitLog): Boolean {
        val id = visitLog.artworkId ?: visitLog.venueId ?: return false
        var inserted = false
        context.dataStore.edit { pref ->
            val current = pref[stampsKey] ?: emptySet()
            if (!current.contains(id)) {
                pref[stampsKey] = current + id
                inserted = true
            }
        }
        return inserted
    }
}

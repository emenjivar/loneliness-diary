package com.emenjivar.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// TODO: move this instance to hilt
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AppDataStore(private val context: Context) {
    private val counter = intPreferencesKey("example_counter")
    val counterFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[counter] ?: 0
    }

    suspend fun setCounter(value: Int) {
        context.dataStore.edit { settings ->
            settings[counter] = value
        }
    }
}

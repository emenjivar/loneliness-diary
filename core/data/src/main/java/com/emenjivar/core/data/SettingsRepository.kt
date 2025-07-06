package com.emenjivar.core.data

import android.content.Context
import com.emenjivar.core.datastore.AppDataStore
import kotlinx.coroutines.flow.distinctUntilChanged

class SettingsRepository(private val context: Context) {
    private val dataStore = AppDataStore(context)

    val counter = dataStore.counterFlow.distinctUntilChanged()
    suspend fun setCounter(value: Int) {
        dataStore.setCounter(value)
    }
}
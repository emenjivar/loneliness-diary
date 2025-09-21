package com.emenjivar.core.data.repositories

import android.content.Context
import com.emenjivar.core.datastore.AppDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

interface SettingsRepository {
    val counter: Flow<Int>

    suspend fun setCounter(value: Int)
}

class SettingsRepositoryImp(
    context: Context
) : SettingsRepository {
    private val dataStore = AppDataStore(context)

    override val counter = dataStore.counterFlow.distinctUntilChanged()

    override suspend fun setCounter(value: Int) {
        dataStore.setCounter(value)
    }
}

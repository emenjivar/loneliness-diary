package com.emenjivar.core.data.repositories

import com.emenjivar.core.data.models.DiaryEntry
import com.emenjivar.core.data.models.toEntity
import com.emenjivar.core.data.models.toModel
import com.emenjivar.core.database.daos.DiaryEntryDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

interface DiaryEntryRepository {
    fun getAll(): Flow<List<DiaryEntry>>
    suspend fun insert(entry: DiaryEntry)
}

// TODO: change scope to internal
class DiaryEntryRepositoryImp(
    private val diaryEntryDao: DiaryEntryDao
) : DiaryEntryRepository {
    override fun getAll() = diaryEntryDao.getAll()
        .distinctUntilChanged()
        .map { entities ->
            entities.map { it.toModel() }
        }

    override suspend fun insert(entry: DiaryEntry) {
        diaryEntryDao.insert(entry.toEntity())
    }
}

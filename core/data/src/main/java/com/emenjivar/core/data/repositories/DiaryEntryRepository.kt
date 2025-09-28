package com.emenjivar.core.data.repositories

import com.emenjivar.core.data.models.DiaryEntry
import com.emenjivar.core.data.models.toEntity
import com.emenjivar.core.data.models.toModel
import com.emenjivar.core.database.daos.DiaryEntryDao
import com.emenjivar.core.database.entities.DiaryEntryWithInsertionsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

interface DiaryEntryRepository {
    fun getAll(): Flow<List<DiaryEntry>>

    fun findById(id: Long): Flow<DiaryEntry?>

    suspend fun insert(entry: DiaryEntry)
}

internal class DiaryEntryRepositoryImp(
    private val diaryEntryDao: DiaryEntryDao
) : DiaryEntryRepository {
    override fun getAll() = diaryEntryDao.getAll()
        .distinctUntilChanged()
        .map { entities ->
            entities.map { it.toModel() }
        }

    override fun findById(id: Long): Flow<DiaryEntry?> {
        return diaryEntryDao.getEntryWithInsertions(id)
            .map { data -> data?.toModel() }
    }

    override suspend fun insert(entry: DiaryEntry) {
        diaryEntryDao.insertDiaryEntryWithEmotions(entry.toEntity())
    }
}

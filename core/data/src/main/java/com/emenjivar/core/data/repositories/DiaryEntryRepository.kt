package com.emenjivar.core.data.repositories

import androidx.room.Transaction
import com.emenjivar.core.data.models.DiaryEntry
import com.emenjivar.core.data.models.toEntity
import com.emenjivar.core.data.models.toModel
import com.emenjivar.core.database.daos.DiaryEntryDao
import com.emenjivar.core.database.daos.DiaryEntryEmotionDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

interface DiaryEntryRepository {
    fun getAll(): Flow<List<DiaryEntry>>

    fun findById(id: Long): Flow<DiaryEntry?>

    suspend fun insert(entry: DiaryEntry)
}

internal class DiaryEntryRepositoryImp(
    private val diaryEntryDao: DiaryEntryDao,
    private val diaryEntryEmotionDao: DiaryEntryEmotionDao
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

    @Transaction
    override suspend fun insert(entry: DiaryEntry) {
        val now = LocalDateTime.now()
        val originalEntry = diaryEntryDao.getById(id = entry.id)

        val entity = entry.copy(
            // Keeps original createdAt if the entry already exists
            createdAt = originalEntry?.createdAt ?: entry.createdAt,
            // Fill updatedAt only if the entry already exists
            updatedAt = if (originalEntry != null) now else null
        ).toEntity()
        val entryId = diaryEntryDao.insert(entity.entry)

        if (entity.entry.id != 0L) {
            diaryEntryEmotionDao.deleteByEntryId(entryId)
        }

        val emotions = entity.emotions.map { emotion ->
            emotion.copy(entryId = entryId)
        }
        diaryEntryEmotionDao.insert(emotions)
    }
}

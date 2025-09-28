package com.emenjivar.core.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.emenjivar.core.database.entities.DiaryEntryEmotionEntity
import com.emenjivar.core.database.entities.DiaryEntryEntity
import com.emenjivar.core.database.entities.DiaryEntryWithInsertionsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryEntryDao {
    @Query("SELECT * FROM diary_entry")
    fun getAll(): Flow<List<DiaryEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: DiaryEntryEntity): Long

    @Insert
    suspend fun insertEmotions(emotions: List<DiaryEntryEmotionEntity>)

    @Transaction
    @Query("SELECT * FROM diary_entry WHERE id=:id")
    fun getEntryWithInsertions(id: Long): Flow<DiaryEntryWithInsertionsEntity?>

    @Query("DELETE FROM diary_entry_emotion WHERE entry_id=:entryId")
    suspend fun deleteEmotionsByEntryId(entryId: Long)

    @Transaction
    suspend fun insertDiaryEntryWithEmotions(diaryEntryWithInsertionsEntity: DiaryEntryWithInsertionsEntity) {
        val entryId = insert(diaryEntryWithInsertionsEntity.entry)

        if (diaryEntryWithInsertionsEntity.entry.id != 0L) {
            // Remove emotions if the entry is already created
            deleteEmotionsByEntryId(entryId = diaryEntryWithInsertionsEntity.entry.id)
        }

        // Update emotions with the correct entry_id
        val emotions = diaryEntryWithInsertionsEntity.emotions.map { emotion ->
            emotion.copy(entryId = entryId)
        }
        insertEmotions(emotions)
    }
}

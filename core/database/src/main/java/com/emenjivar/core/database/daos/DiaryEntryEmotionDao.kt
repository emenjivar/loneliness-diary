package com.emenjivar.core.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.emenjivar.core.database.entities.DiaryEntryEmotionEntity

@Dao
interface DiaryEntryEmotionDao {
    @Insert
    suspend fun insert(emotions: List<DiaryEntryEmotionEntity>)

    @Query("DELETE FROM diary_entry_emotion WHERE entry_id=:entryId")
    suspend fun deleteByEntryId(entryId: Long)
}

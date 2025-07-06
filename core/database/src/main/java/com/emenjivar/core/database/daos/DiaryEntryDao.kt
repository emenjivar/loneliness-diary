package com.emenjivar.core.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.emenjivar.core.database.entities.DiaryEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryEntryDao {
    @Query("SELECT * FROM diary_entry")
    fun getAll(): Flow<List<DiaryEntryEntity>>

    @Insert
    suspend fun insert(entry: DiaryEntryEntity)
}
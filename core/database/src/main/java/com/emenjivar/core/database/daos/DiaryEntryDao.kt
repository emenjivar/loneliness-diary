package com.emenjivar.core.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.emenjivar.core.database.entities.DiaryEntryEntity
import com.emenjivar.core.database.entities.DiaryEntryWithInsertionsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryEntryDao {
    @Query("SELECT * FROM diary_entry")
    fun getAll(): Flow<List<DiaryEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: DiaryEntryEntity): Long

    @Transaction
    @Query("SELECT * FROM diary_entry WHERE id=:id")
    fun getEntryWithInsertions(id: Long): Flow<DiaryEntryWithInsertionsEntity?>
}

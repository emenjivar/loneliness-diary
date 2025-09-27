package com.emenjivar.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.emenjivar.core.database.daos.DiaryEntryDao
import com.emenjivar.core.database.entities.DiaryEntryEmotionEntity
import com.emenjivar.core.database.entities.DiaryEntryEntity

@Database(
    entities = [DiaryEntryEntity::class, DiaryEntryEmotionEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun diaryEntryDao(): DiaryEntryDao
}

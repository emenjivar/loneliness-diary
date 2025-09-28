package com.emenjivar.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.emenjivar.core.database.converters.DateConverters
import com.emenjivar.core.database.daos.DiaryEntryDao
import com.emenjivar.core.database.daos.DiaryEntryEmotionDao
import com.emenjivar.core.database.entities.DiaryEntryEmotionEntity
import com.emenjivar.core.database.entities.DiaryEntryEntity

@Database(
    entities = [DiaryEntryEntity::class, DiaryEntryEmotionEntity::class],
    version = 1
)
@TypeConverters(DateConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun diaryEntryDao(): DiaryEntryDao

    abstract fun diaryEntryEmotionDao(): DiaryEntryEmotionDao
}

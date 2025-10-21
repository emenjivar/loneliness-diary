package com.emenjivar.core.database.di

import android.content.Context
import androidx.room.Room
import com.emenjivar.core.database.AppDatabase
import com.emenjivar.core.database.daos.DiaryEntryDao
import com.emenjivar.core.database.daos.DiaryEntryEmotionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    fun providesDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context = context,
        AppDatabase::class.java,
        "loneliness_diary_db"
    ).build()

    @Provides
    fun providesDiaryEntryDao(
        database: AppDatabase
    ): DiaryEntryDao = database.diaryEntryDao()

    @Provides
    fun providesDiaryEntryEmotionDao(
        database: AppDatabase
    ): DiaryEntryEmotionDao = database.diaryEntryEmotionDao()
}

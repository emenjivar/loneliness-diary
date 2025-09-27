package com.emenjivar.core.data.di

import android.content.Context
import com.emenjivar.core.data.repositories.DiaryEntryRepository
import com.emenjivar.core.data.repositories.DiaryEntryRepositoryImp
import com.emenjivar.core.data.repositories.EmotionsRepository
import com.emenjivar.core.data.repositories.EmotionsRepositoryImp
import com.emenjivar.core.data.repositories.SettingsRepository
import com.emenjivar.core.data.repositories.SettingsRepositoryImp
import com.emenjivar.core.database.daos.DiaryEntryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun providesSettingsRepository(
        @ApplicationContext context: Context
    ): SettingsRepository = SettingsRepositoryImp(
        context = context
    )

    @Provides
    fun providesDiaryEntryRepository(
        diaryEntryDao: DiaryEntryDao
    ): DiaryEntryRepository = DiaryEntryRepositoryImp(
        diaryEntryDao = diaryEntryDao
    )

    @Provides
    fun providesEmotionsRepository(): EmotionsRepository = EmotionsRepositoryImp()
}

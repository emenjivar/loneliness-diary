package com.emenjivar.feature.diary.di

import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.exoplayer.ExoPlayer
import com.emenjivar.feature.diary.ui.ExoplayerProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MediaModule {
    @Provides
    @Singleton
    fun providesExoplayer(
        @ApplicationContext context: Context
    ): ExoPlayer {
        return ExoPlayer.Builder(context)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .setUsage(C.USAGE_MEDIA)
                    .build(),
                // Automatically handles audio focus
                true
            )
            .build()
    }

    @Provides
    @Singleton
    fun providesExoplayerProvider(
        exoPlayer: ExoPlayer
    ): ExoplayerProvider {
        return ExoplayerProvider(exoPlayer = exoPlayer)
    }
}

package com.emenjivar.feature.diary.di

import android.content.Context
import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import com.emenjivar.feature.diary.ui.ImageLoaderProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImageLoaderModule {
    private const val MEMORY_CACHE_MAX_SIZE_PERCENT = 0.25
    private const val DISK_CACHE_MAX_SIZE_PERCENT = 0.02

    @Provides
    @Singleton
    fun providesImageLoader(
        @ApplicationContext context: Context
    ): ImageLoader {
        return ImageLoader.Builder(context)
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, MEMORY_CACHE_MAX_SIZE_PERCENT)
                    .build()
            }.diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .maxSizePercent(DISK_CACHE_MAX_SIZE_PERCENT)
                    .build()
            }.build()
    }

    @Provides
    @Singleton
    fun providesImageLoaderProvider(
        imageLoader: ImageLoader
    ): ImageLoaderProvider {
        return ImageLoaderProvider(loader = imageLoader)
    }
}

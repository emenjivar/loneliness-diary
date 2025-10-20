package com.emenjivar.feature.diary.ui

import androidx.compose.runtime.compositionLocalOf
import androidx.media3.exoplayer.ExoPlayer
import coil3.ImageLoader

val LocalCoilImageLoaderProvider = compositionLocalOf<ImageLoaderProvider> {
    error("No image loader provided")
}

val LocalExoplayerProvider = compositionLocalOf<ExoplayerProvider> {
    error("No exoplayer provided")
}

// Wraps instances to inject them into MainActivity
// without including their respective dependencies in :app
data class ImageLoaderProvider(val loader: ImageLoader)
data class ExoplayerProvider(val exoPlayer: ExoPlayer)
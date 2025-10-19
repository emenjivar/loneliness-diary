package com.emenjivar.core.data.models

import com.emenjivar.network.models.SongNetwork

data class SongModel(
    val id: Long,
    val title: String,
    val previewUrl: String,
    val albumName: String,
    val albumCover: String,
    val albumCoverSmall: String
)

fun SongNetwork.toModel() = SongModel(
    id = id,
    title = title,
    previewUrl = preview,
    albumName = album.title,
    albumCover = album.cover,
    albumCoverSmall = album.coverSmall
)

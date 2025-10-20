package com.emenjivar.core.data.models

import com.emenjivar.network.models.SongNetwork

data class SongModel(
    val id: Long,
    val title: String,
    val artist: String,
    val previewUrl: String,
    val albumId: Long,
    val albumName: String,
    val albumCover: String,
    val albumCoverSmall: String
)

fun SongNetwork.toModel() = SongModel(
    id = id,
    title = title,
    artist = artist.name,
    previewUrl = preview,
    albumId = album.id,
    albumName = album.title,
    albumCover = album.cover,
    albumCoverSmall = album.coverSmall
)

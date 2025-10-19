package com.emenjivar.core.data.models

import com.emenjivar.network.models.SongNetwork

data class SongModel(
    val id: Long,
    val title: String,
    val preview: String,
    val albumName: String,
    val albumCover: String,
    val albumCoverSmall: String
)

fun SongNetwork.toModel() = SongModel(
    id = id,
    title = title,
    preview = preview,
    albumName = album.title,
    albumCover = album.cover,
    albumCoverSmall = album.coverSmall
)

package com.emenjivar.network.models

data class SongNetwork(
    val id: Int,
    val title: String,
    val preview: String,
    val album: AlbumNetwork
)
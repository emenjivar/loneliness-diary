package com.emenjivar.network.models

data class SongNetwork(
    val id: Long,
    val title: String,
    val preview: String,
    val album: AlbumNetwork,
    val artist: ArtistNetwork
)

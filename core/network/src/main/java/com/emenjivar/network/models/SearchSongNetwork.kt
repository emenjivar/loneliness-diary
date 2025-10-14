package com.emenjivar.network.models

data class SearchSongNetwork(
    val data: List<SongNetwork>,
    val total: Int,
    val next: String
)
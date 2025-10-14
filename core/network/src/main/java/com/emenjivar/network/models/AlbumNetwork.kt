package com.emenjivar.network.models

import com.google.gson.annotations.SerializedName

data class AlbumNetwork(
    val title: String,
    val cover: String,

    @SerializedName("cover_small")
    val coverSmall: String
)
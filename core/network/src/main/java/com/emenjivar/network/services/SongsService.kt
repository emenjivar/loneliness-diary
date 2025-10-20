package com.emenjivar.network.services

import com.emenjivar.network.models.SearchSongNetwork
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SongsService {
    @GET("search")
    suspend fun search(
        @Query("q") q: String,
        @Query("limit") limit: Int = 5
    ): Response<SearchSongNetwork>
}

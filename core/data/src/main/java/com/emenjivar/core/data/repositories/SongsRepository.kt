package com.emenjivar.core.data.repositories

import com.emenjivar.core.data.models.SongModel
import com.emenjivar.core.data.models.toModel
import com.emenjivar.core.data.utils.ResultWrapper
import com.emenjivar.network.services.SongsService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface SongsRepository {
    fun search(query: String, limit: Int): Flow<ResultWrapper<List<SongModel>>>
}

class SongsRepositoryImp(
    private val songsService: SongsService
) : SongsRepository {
    override fun search(
        query: String,
        limit: Int
    ): Flow<ResultWrapper<List<SongModel>>> = flow {
        emit(ResultWrapper.Loading)

        val response = songsService
            .search(q = query, limit = limit).body()
            ?.data
            ?.map { it.toModel() }.orEmpty()

        emit(ResultWrapper.Success(response))
    }
}
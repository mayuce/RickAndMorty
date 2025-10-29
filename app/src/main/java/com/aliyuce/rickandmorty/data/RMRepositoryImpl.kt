package com.aliyuce.rickandmorty.data

import com.aliyuce.rickandmorty.data.remote.RMApiService
import com.aliyuce.rickandmorty.data.remote.model.EpisodeResponse
import javax.inject.Inject

class RMRepositoryImpl @Inject constructor(
    private val api: RMApiService,
) : RMRepository {
    override suspend fun getEpisodes(page: Int): Result<EpisodeResponse> = runCatching {
        api.getEpisodes(page = page)
    }
}
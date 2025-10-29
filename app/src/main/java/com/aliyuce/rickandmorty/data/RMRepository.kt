package com.aliyuce.rickandmorty.data

import com.aliyuce.rickandmorty.data.remote.model.EpisodeResponse

interface RMRepository {
    suspend fun getEpisodes(page: Int = 1): Result<EpisodeResponse>
}

package com.aliyuce.rickandmorty.domain

import com.aliyuce.rickandmorty.domain.model.EpisodesPage

interface RMRepository {
    suspend fun getEpisodes(page: Int = 1): Result<EpisodesPage>
}

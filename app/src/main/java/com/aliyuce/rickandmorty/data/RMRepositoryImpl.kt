package com.aliyuce.rickandmorty.data

import com.aliyuce.rickandmorty.data.mapper.toDomain
import com.aliyuce.rickandmorty.data.remote.RMApiService
import com.aliyuce.rickandmorty.domain.RMRepository
import com.aliyuce.rickandmorty.domain.model.EpisodesPage
import com.aliyuce.rickandmorty.domain.model.Character
import javax.inject.Inject

class RMRepositoryImpl
    @Inject
    constructor(
        private val api: RMApiService,
    ) : RMRepository {
        override suspend fun getEpisodes(page: Int): Result<EpisodesPage> =
            runCatching {
                api.getEpisodes(page = page).toDomain()
            }

        override suspend fun getCharacter(id: Int): Result<Character> =
            runCatching {
                api.getCharacter(id = id).toDomain()
            }
    }

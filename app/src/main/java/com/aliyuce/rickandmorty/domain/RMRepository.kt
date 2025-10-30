package com.aliyuce.rickandmorty.domain

import com.aliyuce.rickandmorty.domain.model.EpisodesPage
import com.aliyuce.rickandmorty.domain.model.Character

interface RMRepository {
    suspend fun getEpisodes(page: Int = 1): Result<EpisodesPage>
    suspend fun getCharacter(id: Int): Result<Character>
}

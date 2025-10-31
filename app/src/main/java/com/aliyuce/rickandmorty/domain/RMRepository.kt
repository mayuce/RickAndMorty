package com.aliyuce.rickandmorty.domain

import com.aliyuce.rickandmorty.domain.model.Character
import com.aliyuce.rickandmorty.domain.model.EpisodesPage

interface RMRepository {
    suspend fun getEpisodes(page: Int = 1): Result<EpisodesPage>

    suspend fun getCharacter(id: Int): Result<Character>

    /**
     * Return epoch millis of last time the given episodes page was refreshed (or null)
     */
    suspend fun getLastRefreshedForEpisodesPage(page: Int): Long?

    /**
     * Return epoch millis of last time the given character was refreshed (or null)
     */
    suspend fun getLastRefreshedForCharacter(id: Int): Long?
}

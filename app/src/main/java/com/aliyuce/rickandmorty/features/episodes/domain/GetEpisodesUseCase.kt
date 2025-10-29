package com.aliyuce.rickandmorty.features.episodes.domain

import com.aliyuce.rickandmorty.data.RMRepository
import com.aliyuce.rickandmorty.data.remote.model.EpisodeResponse
import javax.inject.Inject

class GetEpisodesUseCase @Inject constructor(
    private val repository: RMRepository,
) {
    suspend operator fun invoke(page: Int = 1): Result<EpisodeResponse> =
        repository.getEpisodes(page = page)
}

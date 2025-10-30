package com.aliyuce.rickandmorty.features.episodes.domain

import com.aliyuce.rickandmorty.domain.RMRepository
import com.aliyuce.rickandmorty.domain.model.EpisodesPage
import javax.inject.Inject

class GetEpisodesUseCase
    @Inject
    constructor(
        private val repository: RMRepository,
    ) {
        suspend operator fun invoke(page: Int = 1): Result<EpisodesPage> = repository.getEpisodes(page = page)
    }

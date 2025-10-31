package com.aliyuce.rickandmorty.features.episodes.domain

import com.aliyuce.rickandmorty.domain.RMRepository
import javax.inject.Inject

class GetEpisodesLastRefreshedUseCase
    @Inject
    constructor(
        private val repository: RMRepository,
    ) {
        suspend operator fun invoke(page: Int): Long? = repository.getLastRefreshedForEpisodesPage(page)
    }

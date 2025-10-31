package com.aliyuce.rickandmorty.features.characterdetail.domain

import com.aliyuce.rickandmorty.domain.RMRepository
import javax.inject.Inject

class GetCharacterLastRefreshedUseCase
    @Inject
    constructor(
        private val repository: RMRepository,
    ) {
        suspend operator fun invoke(id: Int): Long? = repository.getLastRefreshedForCharacter(id)
    }

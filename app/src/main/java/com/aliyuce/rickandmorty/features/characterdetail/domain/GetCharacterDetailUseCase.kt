package com.aliyuce.rickandmorty.features.characterdetail.domain

import com.aliyuce.rickandmorty.domain.RMRepository
import com.aliyuce.rickandmorty.domain.model.Character
import javax.inject.Inject

class GetCharacterDetailUseCase
    @Inject
    constructor(
        private val repository: RMRepository,
    ) {
        suspend operator fun invoke(id: Int): Result<Character> = repository.getCharacter(id = id)
    }

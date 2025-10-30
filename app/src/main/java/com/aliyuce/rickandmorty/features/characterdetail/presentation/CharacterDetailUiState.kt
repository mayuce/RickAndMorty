package com.aliyuce.rickandmorty.features.characterdetail.presentation

import com.aliyuce.rickandmorty.domain.model.Character

sealed interface CharacterDetailUiState {
    object Loading : CharacterDetailUiState

    data class Success(
        val character: Character,
    ) : CharacterDetailUiState

    data class Error(
        val throwable: Throwable,
    ) : CharacterDetailUiState
}


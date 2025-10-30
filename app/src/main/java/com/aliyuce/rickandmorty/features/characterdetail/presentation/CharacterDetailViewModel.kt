package com.aliyuce.rickandmorty.features.characterdetail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliyuce.rickandmorty.features.characterdetail.domain.GetCharacterDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel
    @Inject
    constructor(
        private val getCharacterDetailUseCase: GetCharacterDetailUseCase,
    ) : ViewModel() {
        private val _uiState: MutableStateFlow<CharacterDetailUiState> = MutableStateFlow(CharacterDetailUiState.Loading)
        val uiState: StateFlow<CharacterDetailUiState> = _uiState

        fun loadCharacter(id: Int) {
            viewModelScope.launch {
                val result = getCharacterDetailUseCase(id)
                result
                    .onSuccess { character ->
                        _uiState.value = CharacterDetailUiState.Success(character = character)
                    }.onFailure { throwable ->
                        _uiState.value = CharacterDetailUiState.Error(throwable = throwable)
                    }
            }
        }
    }

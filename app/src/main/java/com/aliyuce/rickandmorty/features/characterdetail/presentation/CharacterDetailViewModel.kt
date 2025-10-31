package com.aliyuce.rickandmorty.features.characterdetail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliyuce.rickandmorty.features.characterdetail.domain.GetCharacterDetailUseCase
import com.aliyuce.rickandmorty.features.characterdetail.domain.GetCharacterLastRefreshedUseCase
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
        private val getCharacterLastRefreshedUseCase: GetCharacterLastRefreshedUseCase,
    ) : ViewModel() {
        private val _uiState: MutableStateFlow<CharacterDetailUiState> =
            MutableStateFlow(CharacterDetailUiState.Loading)
        val uiState: StateFlow<CharacterDetailUiState> = _uiState

        fun loadCharacter(id: Int) {
            viewModelScope.launch {
                val result = getCharacterDetailUseCase(id)
                result
                    .onSuccess { character ->
                        val lastRefreshed =
                            try {
                                getCharacterLastRefreshedUseCase(id)
                            } catch (_: Exception) {
                                null
                            }
                        _uiState.value =
                            CharacterDetailUiState.Success(
                                character = character,
                                lastRefreshed = lastRefreshed,
                            )
                    }.onFailure { throwable ->
                        _uiState.value = CharacterDetailUiState.Error(throwable = throwable)
                    }
            }
        }
    }

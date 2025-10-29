package com.aliyuce.rickandmorty.features.episodes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliyuce.rickandmorty.features.episodes.domain.GetEpisodesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EpisodesViewModel @Inject constructor(
    private val getEpisodesUseCase: GetEpisodesUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<EpisodesUiState> = MutableStateFlow(EpisodesUiState())
    val uiState: StateFlow<EpisodesUiState> = _uiState

    init {
        loadEpisodes()
    }

    fun loadEpisodes(page: Int = 1) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            val result = getEpisodesUseCase(page)
            result.onSuccess { response ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    episodes = response.results,
                    page = page,
                    totalPages = response.info.pages,
                    error = null
                )
            }.onFailure { throwable ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = throwable.message ?: "Unknown error"
                )
            }
        }
    }
}

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
class EpisodesViewModel
    @Inject
    constructor(
        private val getEpisodesUseCase: GetEpisodesUseCase,
    ) : ViewModel() {
        private val _uiState: MutableStateFlow<EpisodesUiState> =
            MutableStateFlow(EpisodesUiState.Loading)
        val uiState: StateFlow<EpisodesUiState> = _uiState

        private var isLoading = false

        init {
            loadEpisodes()
        }

        /**
         * Load episodes for a page. If page == 1 -> initial load / refresh.
         * If page > 1 -> load more (append).
         */
        fun loadEpisodes(page: Int = 1) {
            if (isLoading) return
            isLoading = true

            val currentState = _uiState.value

            if (page == 1 && (currentState !is EpisodesUiState.Success || currentState.episodes.isEmpty())) {
                _uiState.value = EpisodesUiState.Loading
            } else if (page == 1 && currentState is EpisodesUiState.Success) {
                _uiState.value = currentState.copy(isRefreshing = true, error = null)
            } else if (page > 1 && currentState is EpisodesUiState.Success) {
                _uiState.value = currentState.copy(isLoadingMore = true, error = null)
            }

            viewModelScope.launch {
                val result = getEpisodesUseCase(page)
                result
                    .onSuccess { response ->
                        val prevEpisodes =
                            when (val state = _uiState.value) {
                                is EpisodesUiState.Success -> state.episodes
                                else -> emptyList()
                            }

                        val combined = if (page > 1) prevEpisodes + response.results else response.results

                        _uiState.value =
                            EpisodesUiState.Success(
                                episodes = combined,
                                page = page,
                                totalPages = response.info.pages,
                                isRefreshing = false,
                                isLoadingMore = false,
                                error = null,
                            )
                    }.onFailure { throwable ->
                        when (val state = _uiState.value) {
                            is EpisodesUiState.Success -> {
                                _uiState.value =
                                    state.copy(
                                        isRefreshing = false,
                                        isLoadingMore = false,
                                        error = throwable,
                                    )
                            }

                            else -> {
                                _uiState.value = EpisodesUiState.Error(throwable)
                            }
                        }
                    }
                isLoading = false
            }
        }
    }

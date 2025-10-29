package com.aliyuce.rickandmorty.features.episodes.presentation

import com.aliyuce.rickandmorty.data.remote.model.Episode

sealed interface EpisodesUiState {
    object Loading : EpisodesUiState

    data class Success(
        val episodes: List<Episode> = emptyList(),
        val page: Int = 1,
        val totalPages: Int = 1,
        val isRefreshing: Boolean = false,
        val isLoadingMore: Boolean = false,
        val error: Throwable? = null,
    ) : EpisodesUiState

    data class Error(
        val throwable: Throwable,
    ) : EpisodesUiState
}

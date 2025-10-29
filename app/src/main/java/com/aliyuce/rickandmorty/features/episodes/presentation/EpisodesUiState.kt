package com.aliyuce.rickandmorty.features.episodes.presentation

import com.aliyuce.rickandmorty.data.remote.model.Episode

data class EpisodesUiState(
    val isLoading: Boolean = false,
    val episodes: List<Episode> = emptyList(),
    val page: Int = 1,
    val totalPages: Int = 1,
    val error: String? = null,
)


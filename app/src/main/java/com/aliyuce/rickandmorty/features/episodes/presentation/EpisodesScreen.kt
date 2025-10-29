package com.aliyuce.rickandmorty.features.episodes.presentation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.aliyuce.rickandmorty.ui.theme.RickAndMortyTheme

@Composable
fun EpisodesScreen(
    modifier: Modifier = Modifier,
    onEpisodeClick: (String) -> Unit = {},
) {
    val viewModel: EpisodesViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    Episodes(
        modifier = modifier,
        uiState = uiState,
        onEpisodeClick = onEpisodeClick,
        onLoadMore = { nextPage -> viewModel.loadEpisodes(nextPage) }
    )
}

@Composable
private fun Episodes(
    modifier: Modifier = Modifier,
    uiState: EpisodesUiState,
    onEpisodeClick: (String) -> Unit = {},
    onLoadMore: (Int) -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                when (val state = uiState) {
                    is EpisodesUiState.Loading -> CircularProgressIndicator()
                    is EpisodesUiState.Error -> {
                        // TODO Add better error handling UI
                        Text(text = "Error: ${state.message}")
                    }

                    is EpisodesUiState.Success -> {
                        val listState = rememberLazyListState()
                        // Determine if we should load more items based on the scroll position
                        // Trigger when we see the last 3 items
                        val shouldLoadMore = remember {
                            derivedStateOf {
                                val lastVisible =
                                    listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                                val totalItems = listState.layoutInfo.totalItemsCount
                                totalItems > 0 && lastVisible >= totalItems - 3
                            }
                        }
                        LaunchedEffect(
                            shouldLoadMore.value,
                            state.isLoadingMore,
                            state.page,
                            state.totalPages
                        ) {
                            if (shouldLoadMore.value && !state.isLoadingMore && state.page < state.totalPages) {
                                onLoadMore(state.page + 1)
                            }
                        }

                        LazyColumn(state = listState) {
                            items(state.episodes) { episode ->
                                Text(
                                    text = episode.name,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clickable { onEpisodeClick(episode.id.toString()) }
                                        .padding(16.dp)
                                )
                            }

                            if (state.isLoadingMore) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                    }
                                }
                            }

                            state.error?.let {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(MaterialTheme.colorScheme.errorContainer)
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = it.message ?: "Unknown Error", color = MaterialTheme.colorScheme.error)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Preview(
    name = "Phone",
    device = "spec:width=411dp,height=891dp",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi = true,
    showBackground = true
)
@Composable
private fun EpisodesPreview() {
    RickAndMortyTheme {
        Episodes(
            modifier = Modifier.fillMaxSize(),
            uiState = EpisodesUiState.Success(episodes = emptyList()),
            onEpisodeClick = {},
        )
    }
}

@Preview(
    name = "Phone Dark",
    device = "spec:width=411dp,height=891dp",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = true,
    showBackground = true
)
@Composable
private fun EpisodesDarkPreview() {
    RickAndMortyTheme {
        Episodes(
            modifier = Modifier.fillMaxSize(),
            uiState = EpisodesUiState.Loading,
            onEpisodeClick = {},
        )
    }
}
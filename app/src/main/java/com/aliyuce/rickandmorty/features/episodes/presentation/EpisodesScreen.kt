package com.aliyuce.rickandmorty.features.episodes.presentation

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
    val uiState = viewModel.uiState.collectAsState()

    Episodes(
        modifier = modifier,
        uiState = uiState.value,
        onEpisodeClick = onEpisodeClick,
        onRefresh = { viewModel.loadEpisodes() }
    )
}

@Composable
private fun Episodes(
    modifier: Modifier = Modifier,
    uiState: EpisodesUiState,
    onEpisodeClick: (String) -> Unit = {},
    onRefresh: () -> Unit = {},
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
                when {
                    uiState.isLoading -> CircularProgressIndicator()
                    uiState.error != null -> Text(text = "Error: ${uiState.error}")
                    else -> LazyColumn {
                        items(uiState.episodes) { episode ->
                            Text(
                                text = episode.name,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable { onEpisodeClick(episode.id.toString()) }
                                    .padding(16.dp)
                            )
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
            uiState = EpisodesUiState(),
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
            uiState = EpisodesUiState(),
            onEpisodeClick = {},
        )
    }
}
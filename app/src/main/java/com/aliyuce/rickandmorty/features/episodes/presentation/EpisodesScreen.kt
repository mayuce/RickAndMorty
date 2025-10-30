package com.aliyuce.rickandmorty.features.episodes.presentation

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.aliyuce.rickandmorty.R
import com.aliyuce.rickandmorty.ui.components.ErrorComp
import com.aliyuce.rickandmorty.ui.theme.RickAndMortyTheme

@Composable
fun EpisodesScreen(
    onCharacterClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EpisodesViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Episodes(
        uiState = uiState,
        modifier = modifier,
        onCharacterClick = onCharacterClick,
        onLoadMore = { nextPage -> viewModel.loadEpisodes(nextPage) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Episodes(
    uiState: EpisodesUiState,
    modifier: Modifier = Modifier,
    onCharacterClick: (String) -> Unit = {},
    onLoadMore: (Int) -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.episodes_title),
                        style =
                            MaterialTheme.typography.titleLarge.copy(
                                color = MaterialTheme.colorScheme.primary,
                            ),
                    )
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = modifier,
        content = { paddingValues ->
            var openSheetFor by rememberSaveable { mutableStateOf<Int?>(null) }
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                        .padding(paddingValues),
                contentAlignment = Alignment.Center,
            ) {
                when (val state = uiState) {
                    is EpisodesUiState.Loading -> CircularProgressIndicator()
                    is EpisodesUiState.Error -> {
                        ErrorComp(
                            error = state.throwable.message,
                            onRetry = {
                                onLoadMore(1)
                            },
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                        )
                    }

                    is EpisodesUiState.Success -> {
                        val listState = rememberLazyListState()
                        // Determine if we should load more items based on the scroll position
                        // Trigger when we see the last 3 items
                        val shouldLoadMore =
                            remember {
                                derivedStateOf {
                                    val lastVisible =
                                        listState.layoutInfo.visibleItemsInfo
                                            .lastOrNull()
                                            ?.index ?: 0
                                    val totalItems = listState.layoutInfo.totalItemsCount
                                    totalItems > 0 && lastVisible >= totalItems - 3
                                }
                            }
                        LaunchedEffect(
                            shouldLoadMore.value,
                            state.isLoadingMore,
                            state.page,
                            state.totalPages,
                        ) {
                            if (shouldLoadMore.value && !state.isLoadingMore && state.page < state.totalPages) {
                                onLoadMore(state.page + 1)
                            }
                        }

                        LazyColumn(state = listState) {
                            items(state.episodes) { episode ->
                                EpisodeItem(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                openSheetFor = episode.id
                                            }
                                            .padding(16.dp),
                                    episode = episode,
                                )
                            }

                            if (state.isLoadingMore) {
                                item {
                                    Box(
                                        modifier =
                                            Modifier
                                                .fillMaxSize()
                                                .padding(16.dp),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                    }
                                }
                            }

                            state.error?.let {
                                item {
                                    ErrorComp(
                                        error = it.message,
                                        onRetry = {
                                            onLoadMore(state.page + 1)
                                        },
                                        modifier =
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                    )
                                }
                            }
                        }
                    }
                }
            }
            openSheetFor
                ?.takeIf { uiState is EpisodesUiState.Success }
                ?.let { id ->
                    (uiState as? EpisodesUiState.Success)
                        ?.episodes
                        ?.first { it.id == id }
                        ?.characters
                }
                ?.let { ids ->
                    CharactersSheet(
                        onDismissRequest = { openSheetFor = null },
                        characterIds = ids,
                        onClickId = { id ->
                            onCharacterClick(id)
                            openSheetFor = null
                        },
                        modifier = Modifier.padding(paddingValues)
                    )
                }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersSheet(
    onDismissRequest: () -> Unit,
    characterIds: List<String>,
    onClickId: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest
    ) {
        Text(
            stringResource(R.string.characters_sheet_title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
        LazyColumn {
            items(characterIds, key = { it }) { id ->
                ListItem(
                    headlineContent = { Text(id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onClickId(id)
                        }
                )
                HorizontalDivider()
            }
        }
    }
}

@Preview(
    name = "Phone",
    device = "spec:width=411dp,height=891dp",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi = true,
    showBackground = true,
)
@Composable
private fun EpisodesPreview() {
    RickAndMortyTheme {
        Episodes(
            modifier = Modifier.fillMaxSize(),
            uiState = EpisodesUiState.Success(episodes = emptyList()),
            onCharacterClick = {},
        )
    }
}

@Preview(
    name = "Phone Dark",
    device = "spec:width=411dp,height=891dp",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = true,
    showBackground = true,
)
@Composable
private fun EpisodesDarkPreview() {
    RickAndMortyTheme {
        Episodes(
            modifier = Modifier.fillMaxSize(),
            uiState = EpisodesUiState.Loading,
            onCharacterClick = {},
        )
    }
}

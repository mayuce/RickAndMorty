package com.aliyuce.rickandmorty.features.characterdetail.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.aliyuce.rickandmorty.R
import com.aliyuce.rickandmorty.ui.components.ErrorComp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    modifier: Modifier = Modifier,
    characterId: Int,
    onBack: () -> Unit = {},
    viewModel: CharacterDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = characterId) {
        if (characterId >= 0) viewModel.loadCharacter(characterId)
    }

    Surface(modifier = modifier.fillMaxSize()) {
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                LargeTopAppBar(
                    title = { Text(text = stringResource(R.string.character_detail_title)) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.Default.ArrowBackIosNew,
                                contentDescription = stringResource(R.string.back_content_description)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    scrollBehavior = scrollBehavior
                )
            }
        ) { innerPadding ->

            val listState = rememberLazyListState()

            val firstOffsetPx by remember {
                derivedStateOf {
                    if (listState.firstVisibleItemIndex == 0) listState.firstVisibleItemScrollOffset else 0
                }
            }

            val density = LocalDensity.current
            val parallaxOffsetDp: Dp = with(density) { (firstOffsetPx / 2f).toDp() }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = innerPadding
            ) {
                when (val state = uiState) {
                    is CharacterDetailUiState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    is CharacterDetailUiState.Error -> {
                        item {
                            ErrorComp(
                                error = state.throwable.message,
                                onRetry = { viewModel.loadCharacter(characterId) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }

                    is CharacterDetailUiState.Success -> {
                        val character = state.character

                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                            ) {
                                AsyncImage(
                                    model = character.image,
                                    contentDescription = character.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(220.dp)
                                        .offset(y = -parallaxOffsetDp)
                                        .blur(12.dp)
                                )

                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.36f))
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(16.dp)
                                ) {
                                    AsyncImage(
                                        model = character.image,
                                        contentDescription = character.name,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .padding(end = 16.dp)
                                            .clip(CircleShape)
                                            .size(96.dp)
                                    )

                                    Column {
                                        Text(
                                            text = character.name,
                                            style = MaterialTheme.typography.headlineSmall
                                        )
                                        Text(
                                            text = "${character.status} • ${character.species}",
                                            fontSize = 14.sp
                                        )
                                        Text(text = stringResource(
                                            R.string.origin,
                                            character.origin
                                        ), fontSize = 12.sp)
                                    }
                                }
                            }
                        }

                        item {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = stringResource(R.string.appears_in))
                            }
                        }

                        items(character.episode) { ep ->
                            Text(text = ep, modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp))
                        }
                    }
                }
            }
        }
    }
}

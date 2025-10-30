package com.aliyuce.rickandmorty.features.characterdetail.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
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
        Column {
            CenterAlignedTopAppBar(
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
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )


            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxSize()
            ) {

                when (val state = uiState) {
                    is CharacterDetailUiState.Loading -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is CharacterDetailUiState.Error -> {
                        ErrorComp(
                            error = state.throwable.message,
                            onRetry = { viewModel.loadCharacter(characterId) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    is CharacterDetailUiState.Success -> {
                        val character = state.character
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
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
                                    Text(text = "Origin: ${character.origin}", fontSize = 12.sp)
                                }
                            }

                            // Episodes list
                            Text(
                                text = stringResource(R.string.appears_in),
                                modifier = Modifier.padding(top = 16.dp)
                            )
                            character.episode.forEach { ep ->
                                Text(text = ep, modifier = Modifier.padding(top = 4.dp))
                            }
                        }
                    }
                }

            }
        }
    }
}

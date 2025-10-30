package com.aliyuce.rickandmorty.features.characterdetail.presentation

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.aliyuce.rickandmorty.R
import com.aliyuce.rickandmorty.ui.components.ErrorComp
import kotlinx.coroutines.launch

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

        val context = LocalContext.current
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        val exportSuccessText = stringResource(R.string.export_success)
        val exportFailedTemplate = stringResource(R.string.export_failed)
        val exportNoCharacterText = stringResource(R.string.export_no_character)
        val exportCharacterDescription = stringResource(R.string.export_character_description)
        val exportOpenLabel = stringResource(R.string.export_open)
        val noAppToOpenText = stringResource(R.string.no_app_to_open)

        val createDocumentLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.CreateDocument("text/plain")
        ) { uri ->
            if (uri != null) {
                try {
                    val text = when (val state = uiState) {
                        is CharacterDetailUiState.Success -> {
                            val c = state.character
                            "Name: ${c.name}\nStatus: ${c.status}\nSpecies: ${c.species}\nOrigin: ${c.origin}\nTotal episodes: ${c.episode.size}\n"
                        }

                        else -> "No character data"
                    }

                    context.contentResolver.openOutputStream(uri)
                        ?.use { it.write(text.toByteArray()) }

                    scope.launch {
                        val result = snackbarHostState.showSnackbar(exportSuccessText, exportOpenLabel)
                        if (result == ActionPerformed) {
                            try {
                                val openIntent = Intent(Intent.ACTION_VIEW).apply {
                                    setDataAndType(uri, context.contentResolver.getType(uri))
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                context.startActivity(openIntent)
                            } catch (e: ActivityNotFoundException) {
                                snackbarHostState.showSnackbar(noAppToOpenText)
                            }
                        }
                    }
                } catch (e: Exception) {
                    val msg = e.message ?: ""
                    val formatted = String.format(exportFailedTemplate, msg)
                    scope.launch { snackbarHostState.showSnackbar(formatted) }
                }
            }
        }

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                LargeTopAppBar(
                    title = { Text(text = stringResource(R.string.character_detail_title)) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.Default.ArrowBackIosNew,
                                contentDescription = stringResource(R.string.back_content_description),
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            if (uiState is CharacterDetailUiState.Success) {
                                val name =
                                    (uiState as CharacterDetailUiState.Success).character.name
                                        .replace(Regex("[^A-Za-z0-9_.-]"), "_")
                                createDocumentLauncher.launch("$name.txt")
                            } else {
                                scope.launch { snackbarHostState.showSnackbar(exportNoCharacterText) }
                            }
                        }) {
                            Icon(
                                Icons.Default.Share,
                                contentDescription = exportCharacterDescription
                            )
                        }
                    },
                    colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        ),
                    scrollBehavior = scrollBehavior,
                )
            },
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
                modifier =
                    Modifier
                        .fillMaxSize(),
                contentPadding = innerPadding,
            ) {
                when (val state = uiState) {
                    is CharacterDetailUiState.Loading -> {
                        item {
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxSize(),
                                contentAlignment = Alignment.Center,
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
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                            )
                        }
                    }

                    is CharacterDetailUiState.Success -> {
                        val character = state.character

                        item {
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .height(220.dp),
                            ) {
                                AsyncImage(
                                    model = character.image,
                                    contentDescription = character.name,
                                    contentScale = ContentScale.Crop,
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .height(220.dp)
                                            .offset(y = -parallaxOffsetDp)
                                            .blur(12.dp),
                                )

                                Box(
                                    modifier =
                                        Modifier
                                            .matchParentSize()
                                            .background(
                                                MaterialTheme.colorScheme.background.copy(
                                                    alpha = 0.36f
                                                )
                                            ),
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier =
                                        Modifier
                                            .align(Alignment.BottomStart)
                                            .padding(16.dp),
                                ) {
                                    AsyncImage(
                                        model = character.image,
                                        contentDescription = character.name,
                                        contentScale = ContentScale.Crop,
                                        modifier =
                                            Modifier
                                                .padding(end = 16.dp)
                                                .clip(CircleShape)
                                                .size(96.dp),
                                    )

                                    Column {
                                        Text(
                                            text = character.name,
                                            style = MaterialTheme.typography.headlineSmall,
                                        )
                                        Text(
                                            text = "${character.status} • ${character.species}",
                                            fontSize = 14.sp,
                                        )
                                        Text(
                                            text =
                                                stringResource(
                                                    R.string.origin,
                                                    character.origin,
                                                ),
                                            fontSize = 12.sp,
                                        )
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
                            Text(
                                text = ep,
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    top = 4.dp,
                                    bottom = 4.dp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

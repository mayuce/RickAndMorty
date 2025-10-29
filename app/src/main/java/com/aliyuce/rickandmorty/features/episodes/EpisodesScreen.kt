package com.aliyuce.rickandmorty.features.episodes

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.aliyuce.rickandmorty.ui.theme.RickAndMortyTheme

@Composable
fun EpisodesScreen(
    modifier: Modifier = Modifier,
    onEpisodeClick: (String) -> Unit = {},
) {
    Episodes(
        modifier = modifier,
        onEpisodeClick = onEpisodeClick,
    )
}

@Composable
private fun Episodes(
    modifier: Modifier = Modifier,
    onEpisodeClick: (String) -> Unit = {},
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Hello, Rick and Morty!",
            modifier = Modifier,
        )
    }
}

@Preview(name = "Phone",
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
            onEpisodeClick = {},
        )
    }
}

@Preview(name = "Phone Dark",
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
            onEpisodeClick = {},
        )
    }
}
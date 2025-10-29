package com.aliyuce.rickandmorty.features.episodes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun EpisodesScreen(modifier: Modifier = Modifier, onEpisodeClick: (String) -> Unit = {}) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Hello, Rick and Morty!",
            modifier = Modifier
        )
    }
}

package com.aliyuce.rickandmorty.features.episodes

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.aliyuce.rickandmorty.features.episodes.presentation.EpisodesScreen

object EpisodesRoute {
    const val ROUTE = "episodes_route"
}

fun NavGraphBuilder.episodesRoute(onEpisodeClick: (String) -> Unit = {}) {
    composable(route = EpisodesRoute.ROUTE) {
        EpisodesScreen(
            modifier = Modifier,
            onCharacterClick = onEpisodeClick,
        )
    }
}

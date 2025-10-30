package com.aliyuce.rickandmorty.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.aliyuce.rickandmorty.features.episodes.EpisodesRoute
import com.aliyuce.rickandmorty.features.episodes.episodesRoute

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = EpisodesRoute.ROUTE,
        modifier = modifier,
    ) {
        episodesRoute(
            onEpisodeClick = { episodeId ->
                // Handle episode click navigation here
            },
        )
    }
}

package com.aliyuce.rickandmorty.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aliyuce.rickandmorty.features.episodes.EpisodesScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = "episodes",
        modifier = modifier,
    ) {
        composable("episodes") {
            EpisodesScreen(
                modifier = Modifier,
                onEpisodeClick = { episodeId: String ->
                    // navController.navigate("episodeDetail/$episodeId")
                },
            )
        }
    }
}

package com.aliyuce.rickandmorty.features.characterdetail

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.aliyuce.rickandmorty.features.characterdetail.presentation.CharacterDetailScreen

object CharacterDetailRoute {
    const val ROUTE = "character_detail_route/{characterId}"
    const val ARG_CHARACTER_ID = "characterId"

    fun createRoute(characterId: Int) = "character_detail_route/$characterId"
}

fun NavGraphBuilder.characterDetailRoute(onBack: () -> Unit = {}) {
    composable(
        route = CharacterDetailRoute.ROUTE,
        arguments = listOf(navArgument(CharacterDetailRoute.ARG_CHARACTER_ID) { type = NavType.IntType })
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getInt(CharacterDetailRoute.ARG_CHARACTER_ID) ?: -1
        CharacterDetailScreen(
            modifier = Modifier,
            characterId = id,
            onBack = onBack
        )
    }
}

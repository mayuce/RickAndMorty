package com.aliyuce.rickandmorty.domain.model

import org.jetbrains.annotations.TestOnly

data class Character(
    val id: Int,
    val name: String,
    val status: String = "",
    val species: String = "",
    val image: String = "",
    val origin: String = "",
    val episode: List<String> = emptyList(),
) {
    companion object {
        @TestOnly
        val fake = Character(
            id = 1,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            image = "",
            origin = "Earth",
            episode = listOf("S01E01", "S01E02"),
        )
    }
}

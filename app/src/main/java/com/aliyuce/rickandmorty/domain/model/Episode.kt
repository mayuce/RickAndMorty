package com.aliyuce.rickandmorty.domain.model

import org.jetbrains.annotations.TestOnly

data class Episode(
    val id: Int,
    val name: String,
    val airDate: String,
    val episode: String,
    val characters: List<String> = emptyList(),
) {
    companion object {
        @TestOnly
        val fake1 = Episode(id = 1, name = "Ep1", airDate = "2020-01-01", episode = "S01E01")

        @TestOnly
        val fake2 = Episode(id = 2, name = "Ep2", airDate = "2020-01-08", episode = "S01E02")

        @TestOnly
        val pilot = Episode(
            id = 1,
            name = "Pilot",
            airDate = "December 2, 2013",
            episode = "S01E01",
            characters = listOf("1", "2"),
        )

        @TestOnly
        val lawnmowerDog = Episode(
            id = 2,
            name = "Lawnmower Dog",
            airDate = "December 9, 2013",
            episode = "S01E02",
            characters = listOf("3"),
        )

        @TestOnly
        fun sampleList() = listOf(pilot, lawnmowerDog)

        @TestOnly
        fun pageOf(vararg eps: Episode, count: Int = eps.size, pages: Int = 1) =
            EpisodesPage(count = count, pages = pages, results = eps.toList())
    }
}

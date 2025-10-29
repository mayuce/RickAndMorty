package com.aliyuce.rickandmorty.domain.model

data class EpisodesPage(
    val count: Int,
    val pages: Int,
    val next: String? = null,
    val prev: String? = null,
    val results: List<Episode> = emptyList(),
)

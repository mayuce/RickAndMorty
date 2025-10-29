package com.aliyuce.rickandmorty.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Info(
    val count: Int,
    val pages: Int,
    val next: String? = null,
    val prev: String? = null,
)

@Serializable
data class EpisodeResponse(
    val info: Info,
    val results: List<Episode>,
)

@Serializable
data class Episode(
    val id: Int,
    val name: String,
    @SerialName("air_date") val airDate: String,
    val episode: String,
    val characters: List<String>,
)

@Serializable
data class Character(
    val id: Int,
    val name: String,
    val status: String? = null,
    val species: String? = null,
    val image: String? = null,
    val origin: Origin? = null,
    val episode: List<String> = emptyList(),
)

@Serializable
data class Origin(
    val name: String,
    val url: String? = null,
)

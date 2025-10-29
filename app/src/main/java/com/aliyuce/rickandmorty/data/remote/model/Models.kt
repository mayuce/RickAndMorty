package com.aliyuce.rickandmorty.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Info(
    val count: Int,
    val pages: Int,
    val next: String? = null,
    val prev: String? = null,
)

@JsonClass(generateAdapter = true)
data class EpisodeResponse(
    val info: Info,
    val results: List<Episode>,
)

@JsonClass(generateAdapter = true)
data class Episode(
    val id: Int,
    val name: String,
    @Json(name = "air_date") val airDate: String,
    val episode: String,
    val characters: List<String>,
)

@JsonClass(generateAdapter = true)
data class Character(
    val id: Int,
    val name: String,
    val status: String? = null,
    val species: String? = null,
    val image: String? = null,
    val origin: Origin? = null,
    val episode: List<String> = emptyList(),
)

@JsonClass(generateAdapter = true)
data class Origin(
    val name: String,
    val url: String? = null,
)

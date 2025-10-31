package com.aliyuce.rickandmorty.domain.model

data class Character(
    val id: Int,
    val name: String,
    val status: String = "",
    val species: String = "",
    val image: String = "",
    val origin: String = "",
    val episode: List<String> = emptyList(),
)

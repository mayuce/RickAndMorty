package com.aliyuce.rickandmorty.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "episodes")
data class EpisodeEntity(
    @PrimaryKey val id: Int,
    val name: String,
    @ColumnInfo(name = "air_date") val airDate: String,
    @ColumnInfo(name = "episode_code") val episodeCode: String,
    val characters: List<String> = emptyList(),
    val page: Int = 1,
    @ColumnInfo(name = "last_refreshed") val lastRefreshed: Long? = null,
)

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val status: String = "",
    val species: String = "",
    val image: String = "",
    val origin: String = "",
    val episodes: List<String> = emptyList(),
    @ColumnInfo(name = "last_refreshed") val lastRefreshed: Long? = null,
)

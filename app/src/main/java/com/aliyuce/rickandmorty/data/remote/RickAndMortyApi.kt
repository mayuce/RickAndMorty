package com.aliyuce.rickandmorty.data.remote

import com.aliyuce.rickandmorty.data.remote.model.Character
import com.aliyuce.rickandmorty.data.remote.model.Episode
import com.aliyuce.rickandmorty.data.remote.model.EpisodeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {
    @GET("/api/episode")
    suspend fun getEpisodes(
        @Query("page") page: Int = 1,
    ): EpisodeResponse

    @GET("/api/episode/{id}")
    suspend fun getEpisode(
        @Path("id") id: Int,
    ): Episode

    @GET("/api/character/{id}")
    suspend fun getCharacter(
        @Path("id") id: Int,
    ): Character
}

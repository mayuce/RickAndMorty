package com.aliyuce.rickandmorty.data.mapper

import com.aliyuce.rickandmorty.data.remote.model.EpisodeResponse
import com.aliyuce.rickandmorty.domain.model.EpisodesPage
import com.aliyuce.rickandmorty.data.remote.model.Episode as RemoteEpisode
import com.aliyuce.rickandmorty.domain.model.Episode as DomainEpisode

fun RemoteEpisode.toDomain(): DomainEpisode =
    DomainEpisode(
        id = this.id,
        name = this.name,
        airDate = this.airDate,
        episode = this.episode,
        characters = this.characters,
    )

fun EpisodeResponse.toDomain(): EpisodesPage =
    EpisodesPage(
        count = this.info.count,
        pages = this.info.pages,
        next = this.info.next,
        prev = this.info.prev,
        results = this.results.map { it.toDomain() },
    )

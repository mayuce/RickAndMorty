package com.aliyuce.rickandmorty.data

import com.aliyuce.rickandmorty.data.local.CharacterDao
import com.aliyuce.rickandmorty.data.local.CharacterEntity
import com.aliyuce.rickandmorty.data.local.EpisodeDao
import com.aliyuce.rickandmorty.data.local.EpisodeEntity
import com.aliyuce.rickandmorty.data.mapper.toDomain
import com.aliyuce.rickandmorty.data.remote.RMApiService
import com.aliyuce.rickandmorty.domain.RMRepository
import com.aliyuce.rickandmorty.domain.model.Character
import com.aliyuce.rickandmorty.domain.model.EpisodesPage
import javax.inject.Inject

class RMRepositoryImpl
@Inject
constructor(
    private val api: RMApiService,
    private val episodeDao: EpisodeDao,
    private val characterDao: CharacterDao,
) : RMRepository {
    override suspend fun getEpisodes(page: Int): Result<EpisodesPage> =
        runCatching {
            val response = api.getEpisodes(page = page)
            val now = System.currentTimeMillis()
            val entities =
                response.results.map { re ->
                    EpisodeEntity(
                        id = re.id,
                        name = re.name,
                        airDate = re.airDate,
                        episodeCode = re.episode,
                        characters = re.characters,
                        page = page,
                        lastRefreshed = now,
                    )
                }
            episodeDao.insertAll(entities)
            response.toDomain()
        }.recoverCatching {
            val cached = episodeDao.getByPage(page)
            if (cached.isNotEmpty()) {
                EpisodesPage(
                    count = cached.size,
                    pages = page,
                    next = null,
                    prev = null,
                    results =
                        cached.map { e ->
                            com.aliyuce.rickandmorty.domain.model.Episode(
                                id = e.id,
                                name = e.name,
                                airDate = e.airDate,
                                episode = e.episodeCode,
                                characters = e.characters,
                            )
                        },
                )
            } else {
                throw it
            }
        }

    override suspend fun getCharacter(id: Int): Result<Character> =
        runCatching {
            val response = api.getCharacter(id = id)
            val domain = response.toDomain()
            val now = System.currentTimeMillis()
            val entity =
                CharacterEntity(
                    id = domain.id,
                    name = domain.name,
                    status = domain.status,
                    species = domain.species,
                    image = domain.image,
                    origin = domain.origin,
                    episodes = domain.episode,
                    lastRefreshed = now,
                )
            characterDao.insert(entity)
            domain
        }.recoverCatching {
            val cached = characterDao.getById(id)
            if (cached != null) {
                Character(
                    id = cached.id,
                    name = cached.name,
                    status = cached.status,
                    species = cached.species,
                    image = cached.image,
                    origin = cached.origin,
                    episode = cached.episodes,
                )
            } else {
                throw it
            }
        }

    override suspend fun getLastRefreshedForEpisodesPage(page: Int): Long? =
        episodeDao.getLastRefreshedForPage(page)

    override suspend fun getLastRefreshedForCharacter(id: Int): Long? =
        characterDao.getLastRefreshedForId(id)
}

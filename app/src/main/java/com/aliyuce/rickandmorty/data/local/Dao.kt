package com.aliyuce.rickandmorty.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EpisodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(episodes: List<EpisodeEntity>)

    @Query("SELECT * FROM episodes WHERE page = :page ORDER BY id ASC")
    suspend fun getByPage(page: Int): List<EpisodeEntity>

    @Query("SELECT * FROM episodes ORDER BY id ASC")
    suspend fun getAll(): List<EpisodeEntity>

    @Query("SELECT MAX(last_refreshed) FROM episodes WHERE page = :page")
    suspend fun getLastRefreshedForPage(page: Int): Long?
}

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: CharacterEntity)

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getById(id: Int): CharacterEntity?

    @Query("SELECT last_refreshed FROM characters WHERE id = :id")
    suspend fun getLastRefreshedForId(id: Int): Long?
}

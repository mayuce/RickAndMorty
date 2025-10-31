package com.aliyuce.rickandmorty.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [EpisodeEntity::class, CharacterEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun episodeDao(): EpisodeDao

    abstract fun characterDao(): CharacterDao
}

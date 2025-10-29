package com.aliyuce.rickandmorty.data

import com.aliyuce.rickandmorty.domain.RMRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun bindEpisodesRepository(impl: RMRepositoryImpl): RMRepository
}

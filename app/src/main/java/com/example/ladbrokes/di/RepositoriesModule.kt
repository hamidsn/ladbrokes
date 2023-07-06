package com.example.ladbrokes.di

import com.example.ladbrokes.data.repositories.RaceRepositoryImpl
import com.example.ladbrokes.domain.repositories.RaceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * This class should only be referenced by generated code.
 */
@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoriesModule {

    @Binds
    abstract fun bindCharacterRepository(impl: RaceRepositoryImpl): RaceRepository
}
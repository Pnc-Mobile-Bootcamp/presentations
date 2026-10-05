package com.pnc.jetpackcomposedemos.core

import com.pnc.jetpackcomposedemos.features.artists.data.ArtistDataSource
import com.pnc.jetpackcomposedemos.features.artists.data.DefaultArtistRepository
import com.pnc.jetpackcomposedemos.features.artists.data.HardCodedArtistDataSource
import com.pnc.jetpackcomposedemos.features.artists.data.RandomArtistDataSource
import com.pnc.jetpackcomposedemos.features.artists.domain.ArtistRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppInjectionModule {

    @Binds
    @Singleton
    abstract fun bindArtistDataSource(
        implementation: RandomArtistDataSource
    ): ArtistDataSource

    @Binds
    abstract fun bindArtistRepository(
        implementation: DefaultArtistRepository
    ): ArtistRepository

}



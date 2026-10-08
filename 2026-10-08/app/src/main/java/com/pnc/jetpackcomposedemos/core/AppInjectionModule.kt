package com.pnc.jetpackcomposedemos.core

import com.pnc.jetpackcomposedemos.features.artists.data.ArtistDataSource
import com.pnc.jetpackcomposedemos.features.artists.data.DefaultArtistRepository
import com.pnc.jetpackcomposedemos.features.artists.data.HardCodedArtistDataSource
import com.pnc.jetpackcomposedemos.features.artists.data.RandomArtistDataSource
import com.pnc.jetpackcomposedemos.features.artists.domain.ArtistRepository
import com.pnc.jetpackcomposedemos.features.orders.data.DefaultOrderRepository
import com.pnc.jetpackcomposedemos.features.orders.data.LocalOnlyOrderRepository
import com.pnc.jetpackcomposedemos.features.orders.domain.OrderRepository
import com.pnc.jetpackcomposedemos.features.todo.data.DefaultToDoRepository
import com.pnc.jetpackcomposedemos.features.todo.domain.ToDoRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope

@Module
@InstallIn(SingletonComponent::class)
abstract class AppInjectionModule {


    @Binds
    abstract fun bindArtistRepository(
        implementation: DefaultArtistRepository
    ): ArtistRepository

    @Binds
    abstract fun bindOrdersRepository(
        implementation: LocalOnlyOrderRepository
    ): OrderRepository

    @Binds
    abstract fun bindToDoRepository(
        implementation: DefaultToDoRepository
    ): ToDoRepository


    companion object {

        @Provides
        @Singleton
        @ApplicationScope
        fun provideAppScope(): CoroutineScope {
            return CoroutineScope(SupervisorJob() + Dispatchers.Default)
            // NOTE: using SupervisorJob keeps one failed child from
            // cancelling the scope for everyone else
        }

    }

}



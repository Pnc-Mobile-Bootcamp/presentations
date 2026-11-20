package com.pnc.jetpackcomposedemos.di

import android.content.Context
import androidx.room.Room
import com.pnc.jetpackcomposedemos.core.room.AppDatabase
import com.pnc.jetpackcomposedemos.core.room.DatabaseModule
import com.pnc.jetpackcomposedemos.features.artists.data.local.ArtistDao
import com.pnc.jetpackcomposedemos.features.orders.data.local.OrderDao
import com.pnc.jetpackcomposedemos.features.todo.data.ToDoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object TestDatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
    }

    @Provides
    @Singleton
    fun provideToDoDao(db: AppDatabase): ToDoDao = db.toDoDao()

    @Provides
    @Singleton
    fun provideOrderDao(db: AppDatabase): OrderDao = db.orderDao()

    @Provides
    @Singleton
    fun provideArtistDao(db: AppDatabase): ArtistDao = db.artistDao()



}





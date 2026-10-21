package com.pnc.jetpackcomposedemos.core

import com.pnc.jetpackcomposedemos.BuildConfig
import com.pnc.jetpackcomposedemos.features.artists.data.ArtistDataSource
import com.pnc.jetpackcomposedemos.features.artists.data.RemoteArtistDataSource
import com.pnc.jetpackcomposedemos.features.orders.data.OrderDataSource
import com.pnc.jetpackcomposedemos.features.orders.data.RemoteOrderDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AdventureWorks


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @AdventureWorks
    fun provideAWRetrofit(
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.AW_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Provides
    @Singleton
    fun provideArtistDataSource(
        retrofit: Retrofit
    ): ArtistDataSource {
        return retrofit.create(
            RemoteArtistDataSource::class.java
        )
    }

    @Provides
    @Singleton
    fun provideOrderDataSource(
        @AdventureWorks retrofit: Retrofit
    ): OrderDataSource {
        return retrofit.create(
            RemoteOrderDataSource::class.java
        )
    }


}
package com.pnc.jetpackcomposedemos.core

import com.pnc.jetpackcomposedemos.features.auth.data.storage.DataStoreTokenStore
import com.pnc.jetpackcomposedemos.features.auth.data.storage.TokenStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TokenStoreModule {

    @Binds
    abstract fun bindTokenStore(implementation: DataStoreTokenStore): TokenStore

}




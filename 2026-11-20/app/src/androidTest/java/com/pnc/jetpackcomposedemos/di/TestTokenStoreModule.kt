package com.pnc.jetpackcomposedemos.di

import com.pnc.jetpackcomposedemos.core.TokenStoreModule
import com.pnc.jetpackcomposedemos.features.auth.data.storage.TokenStore
import com.pnc.jetpackcomposedemos.features.auth.domain.User
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Inject
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [TokenStoreModule::class])
abstract class TestTokenStoreModule {
    @Binds
    abstract fun bindTokenStore(fake: InMemoryTokenStore): TokenStore
}

@Singleton
class InMemoryTokenStore @Inject constructor() : TokenStore {
    private var saved: User? = null
    override suspend fun load(): User? = saved
    override suspend fun save(user: User) { saved = user }
    override suspend fun clear() { saved = null }
}




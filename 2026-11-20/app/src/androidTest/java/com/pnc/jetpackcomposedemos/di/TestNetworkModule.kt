package com.pnc.jetpackcomposedemos.di

import com.pnc.jetpackcomposedemos.core.NetworkModule
import com.pnc.jetpackcomposedemos.features.artists.data.ArtistDataSource
import com.pnc.jetpackcomposedemos.features.orders.data.MyOrdersDataSource
import com.pnc.jetpackcomposedemos.features.orders.data.OrderDataSource
import com.pnc.jetpackcomposedemos.testutil.fakes.FakeArtistDataSource
import com.pnc.jetpackcomposedemos.testutil.fakes.FakeMyOrdersDataSource
import com.pnc.jetpackcomposedemos.testutil.fakes.FakeOrderDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class]
)
abstract class TestNetworkModule {

    @Binds
    abstract fun bindArtistDataSource(fake: FakeArtistDataSource): ArtistDataSource

    @Binds
    abstract fun bindOrderDataSource(fake: FakeOrderDataSource): OrderDataSource

    @Binds
    abstract fun bindMyOrdersDataSource(fake: FakeMyOrdersDataSource): MyOrdersDataSource


}



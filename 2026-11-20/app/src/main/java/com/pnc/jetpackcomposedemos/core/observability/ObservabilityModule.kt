package com.pnc.jetpackcomposedemos.core.observability

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ObservabilityModule {

    @Binds
    abstract fun bindCrashReporter(implementation: LocalCrashReporter): CrashReporter

}



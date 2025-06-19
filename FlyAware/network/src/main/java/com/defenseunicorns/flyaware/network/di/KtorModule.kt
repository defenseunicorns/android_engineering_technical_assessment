package com.defenseunicorns.flyaware.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class KtorModule {
    @Provides
    @Singleton
    fun provideKtor(): HttpClient = HttpClient(CIO) {
        expectSuccess = true
    }
}

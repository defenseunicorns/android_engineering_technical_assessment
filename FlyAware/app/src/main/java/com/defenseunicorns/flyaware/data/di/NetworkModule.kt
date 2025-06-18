package com.defenseunicorns.flyaware.data.di

import com.defenseunicorns.flyaware.data.remote.AviationWeatherApi
import com.defenseunicorns.flyaware.data.remote.AviationWeatherApiImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun bindAviationWeatherApi(
        impl: AviationWeatherApiImpl
    ): AviationWeatherApi
}

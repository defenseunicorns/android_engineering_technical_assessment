package com.defenseunicorns.flyaware.data.di

import com.defenseunicorns.flyaware.domain.repository.FlyAwareRepository
import com.defenseunicorns.flyaware.data.repository.FlyAwareRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindFlyAwareRepository(
        impl: FlyAwareRepositoryImpl
    ): FlyAwareRepository
}

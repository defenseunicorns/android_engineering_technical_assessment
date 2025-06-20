package com.defenseunicorns.flyaware.data.di

import com.defenseunicorns.flyaware.data.local.LocalDataSource
import com.defenseunicorns.flyaware.data.local.LocalDataSourceImpl
import com.defenseunicorns.flyaware.data.remote.RemoteDataSource
import com.defenseunicorns.flyaware.data.remote.RemoteDataSourceImpl
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
    abstract fun bindRemoteDataSource(
        impl: RemoteDataSourceImpl
    ): RemoteDataSource

    @Binds
    @Singleton
    abstract fun bindLocalDataSource(
        impl: LocalDataSourceImpl
    ): LocalDataSource

    @Binds
    @Singleton
    abstract fun bindFlyAwareRepository(
        impl: FlyAwareRepositoryImpl
    ): FlyAwareRepository
}

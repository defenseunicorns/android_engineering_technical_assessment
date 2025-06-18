package com.defenseunicorns.flyaware.data.di

import com.defenseunicorns.flyaware.domain.usecase.FlyAwareUseCases
import com.defenseunicorns.flyaware.domain.usecase.GetMetarsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideFlyAwareUseCases(
        getMetars: GetMetarsUseCase,
    ): FlyAwareUseCases = FlyAwareUseCases(
        getMetars = getMetars,
    )
}

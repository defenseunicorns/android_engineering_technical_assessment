package com.defenseunicorns.flyaware.data.di

import com.defenseunicorns.flyaware.domain.usecase.AddAirportUseCase
import com.defenseunicorns.flyaware.domain.usecase.FlyAwareUseCases
import com.defenseunicorns.flyaware.domain.usecase.GetAirportsUseCase
import com.defenseunicorns.flyaware.domain.usecase.GetMetarsUseCase
import com.defenseunicorns.flyaware.domain.usecase.GetTafsUseCase
import com.defenseunicorns.flyaware.domain.usecase.RemoveAirportUseCase
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
        getTafs: GetTafsUseCase,
        getAirports: GetAirportsUseCase,
        addAirport: AddAirportUseCase,
        removeAirport: RemoveAirportUseCase
    ): FlyAwareUseCases = FlyAwareUseCases(
        getMetars = getMetars,
        getTafs = getTafs,
        getAirports = getAirports,
        addAirport = addAirport,
        removeAirport = removeAirport
    )
}

package com.defenseunicorns.flyaware.domain.usecase

data class FlyAwareUseCases(
    val getMetars: GetMetarsUseCase,
    val getAirports: GetAirportsUseCase,
    val addAirport: AddAirportUseCase,
)
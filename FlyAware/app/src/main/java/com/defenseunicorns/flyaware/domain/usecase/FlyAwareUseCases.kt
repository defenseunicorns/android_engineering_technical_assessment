package com.defenseunicorns.flyaware.domain.usecase

data class FlyAwareUseCases(
    val getMetars: GetMetarsUseCase,
    val getTafs: GetTafsUseCase,
    val getAirports: GetAirportsUseCase,
    val getAirportByIcao: GetAirportByIcaoUseCase,
    val addAirport: AddAirportUseCase,
    val removeAirport: RemoveAirportUseCase
)
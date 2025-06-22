package com.defenseunicorns.flyaware.data.repository

import android.util.Log
import com.defenseunicorns.flyaware.data.local.AirportEntity
import com.defenseunicorns.flyaware.data.local.LocalDataSource
import com.defenseunicorns.flyaware.data.remote.RemoteDataSource
import com.defenseunicorns.flyaware.domain.repository.FlyAwareRepository
import com.defenseunicorns.flyaware.model.Airport
import com.defenseunicorns.flyaware.model.Metar
import com.defenseunicorns.flyaware.model.Taf
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlyAwareRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : FlyAwareRepository {

    companion object {
        private const val TAG = "FlyAwareRepository"
    }

    override suspend fun getMetars(icaoCodes: List<String>): List<Metar> {
        return try {
            Log.d(TAG, "Getting METARs for airports: $icaoCodes")
            
            val result = remoteDataSource.getMetars(icaoCodes)
            result.fold(
                onSuccess = { metarDtos ->
                    Log.d(TAG, "Successfully fetched ${metarDtos.size} METARs")
                    metarDtos.map { it.toMetar() }
                },
                onFailure = { exception ->
                    Log.e(TAG, "Failed to fetch METARs: ${exception.message}")
                    throw exception
                }
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error in getMetars: ${e.message}")
            throw e
        }
    }

    override suspend fun getTafs(icaoCodes: List<String>): List<Taf> {
        return try {
            Log.d(TAG, "Getting TAFs for airports: $icaoCodes")
            
            val result = remoteDataSource.getTafs(icaoCodes)
            result.fold(
                onSuccess = { tafDtos ->
                    Log.d(TAG, "Successfully fetched ${tafDtos.size} TAFs")
                    
                    if (tafDtos.isEmpty()) {
                        Log.d(TAG, "No TAFs available for airports: $icaoCodes - this is normal for some airports")
                        return emptyList()
                    }
                    
                    tafDtos.mapNotNull { dto ->
                        try {
                            Log.d(TAG, "Converting TAF DTO: icaoId=${dto.icaoId}, rawTAF=${dto.rawTAF?.take(50)}")
                            Log.d(TAG, "TAF fcsts count: ${dto.fcsts?.size}")
                            val taf = dto.toTaf()
                            Log.d(TAG, "Successfully converted TAF: ${taf.icaoCode}, rawText=${taf.rawText.take(50)}..., forecastPeriods=${taf.forecastPeriods.size}")
                            taf
                        } catch (e: Exception) {
                            Log.e(TAG, "Failed to convert TAF DTO: ${e.message}")
                            null
                        }
                    }
                },
                onFailure = { exception ->
                    Log.e(TAG, "Failed to fetch TAFs: ${exception.message}")
                    throw exception
                }
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error in getTafs: ${e.message}")
            throw e
        }
    }

    override suspend fun addAirport(icaoCode: String) {
        try {
            Log.d(TAG, "Adding airport: $icaoCode")

            // First validate the airport code
            val validationResult = remoteDataSource.validateAirportCode(icaoCode)
            validationResult.fold(
                onSuccess = { isValid ->
                    if (isValid) {
                        // Get airport information
                        val airportInfoResult = remoteDataSource.getAirportInfo(icaoCode)
                        airportInfoResult.fold(
                            onSuccess = { airportInfo ->
                                val airportEntity = AirportEntity(
                                    icaoCode = airportInfo.icaoCode.orEmpty(),
                                    name = airportInfo.name,
                                    state = airportInfo.state,
                                    country = airportInfo.country,
                                    elevation = airportInfo.elevation,
                                    latitude = airportInfo.latitude,
                                    longitude = airportInfo.longitude
                                )
                                localDataSource.insertAirport(airportEntity)
                                Log.d(TAG, "Successfully added airport: $icaoCode")
                            },
                            onFailure = { exception ->
                                Log.e(TAG, "Failed to get airport info: ${exception.message}")
                                throw exception
                            }
                        )
                    } else {
                        Log.w(TAG, "Invalid airport code: $icaoCode")
                        throw IllegalArgumentException("Invalid airport code: $icaoCode")
                    }
                },
                onFailure = { exception ->
                    Log.e(TAG, "Failed to validate airport code: ${exception.message}")
                    throw exception
                }
            )
        } catch (e: Exception) {
            Log.e("FlyAwareRepository", "Error in addAirport: ${e.message}")
        }
    }

    override suspend fun getSavedAirports(): Flow<List<AirportEntity>> {
        Log.d(TAG, "Getting saved airports")
        return localDataSource.getAllAirports()
    }

    override suspend fun removeAirport(icaoCode: String) {
        try {
            Log.d(TAG, "Removing airport: $icaoCode")
            localDataSource.removeAirport(icaoCode)
            Log.d(TAG, "Successfully removed airport: $icaoCode")
        } catch (e: Exception) {
            Log.e(TAG, "Error in removeAirport: ${e.message}")
            throw e
        }
    }

    override suspend fun getAirportByIcao(icaoCode: String): AirportEntity? {
        Log.d(TAG, "Getting airport by ICAO code: $icaoCode")
        return localDataSource.getAirportByIcaoCode(icaoCode)
    }
}

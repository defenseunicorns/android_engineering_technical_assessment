package com.defenseunicorns.flyaware.data.repository

import android.util.Log
import com.defenseunicorns.flyaware.data.local.AirportEntity
import com.defenseunicorns.flyaware.data.local.LocalDataSource
import com.defenseunicorns.flyaware.data.remote.RemoteDataSource
import com.defenseunicorns.flyaware.domain.repository.FlyAwareRepository
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
                    tafDtos.map { it.toTaf() }
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
                                    icaoCode = airportInfo.icaoCode,
                                    name = airportInfo.name
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
            Log.e(TAG, "Error in addAirport: ${e.message}")
            throw e
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
}

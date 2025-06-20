package com.defenseunicorns.flyaware.data.local

import android.util.Log
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSourceImpl @Inject constructor(
    private val database: FlyAwareDatabase
) : LocalDataSource {

    private val airportDao: AirportDao = database.airportDao()

    companion object {
        private const val TAG = "LocalDataSource"
    }

    override suspend fun insertAirport(airport: AirportEntity) {
        try {
            Log.d(TAG, "Inserting airport: ${airport.icaoCode}")
            airportDao.insertAirport(airport)
            Log.d(TAG, "Successfully inserted airport: ${airport.icaoCode}")
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting airport ${airport.icaoCode}: ${e.message}")
            throw e
        }
    }

    override suspend fun removeAirport(icaoCode: String) {
        try {
            Log.d(TAG, "Removing airport: $icaoCode")
            airportDao.removeAirport(icaoCode)
            Log.d(TAG, "Successfully removed airport: $icaoCode")
        } catch (e: Exception) {
            Log.e(TAG, "Error removing airport $icaoCode: ${e.message}")
            throw e
        }
    }

    override fun getAllAirports(): Flow<List<AirportEntity>> {
        Log.d(TAG, "Getting all airports")
        return airportDao.getAllAirports()
    }

    override suspend fun getAirportByIcaoCode(icaoCode: String): AirportEntity? {
        return try {
            Log.d(TAG, "Getting airport by ICAO code: $icaoCode")
            val airport = airportDao.getAirportByIcaoCode(icaoCode)
            if (airport != null) {
                Log.d(TAG, "Found airport: $icaoCode")
            } else {
                Log.d(TAG, "Airport not found: $icaoCode")
            }
            airport
        } catch (e: Exception) {
            Log.e(TAG, "Error getting airport $icaoCode: ${e.message}")
            throw e
        }
    }

    override suspend fun airportExists(icaoCode: String): Boolean {
        return try {
            Log.d(TAG, "Checking if airport exists: $icaoCode")
            val exists = airportDao.airportExists(icaoCode)
            Log.d(TAG, "Airport $icaoCode exists: $exists")
            exists
        } catch (e: Exception) {
            Log.e(TAG, "Error checking if airport exists $icaoCode: ${e.message}")
            throw e
        }
    }

    override suspend fun updateAirport(airport: AirportEntity) {
        try {
            Log.d(TAG, "Updating airport: ${airport.icaoCode}")
            airportDao.updateAirport(airport)
            Log.d(TAG, "Successfully updated airport: ${airport.icaoCode}")
        } catch (e: Exception) {
            Log.e(TAG, "Error updating airport ${airport.icaoCode}: ${e.message}")
            throw e
        }
    }

    override suspend fun clearAllAirports() {
        try {
            Log.d(TAG, "Clearing all airports")
            airportDao.clearAllAirports()
            Log.d(TAG, "Successfully cleared all airports")
        } catch (e: Exception) {
            Log.e(TAG, "Error clearing all airports: ${e.message}")
            throw e
        }
    }
} 
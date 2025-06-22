package com.defenseunicorns.flyaware.data.remote

import android.util.Log
import com.defenseunicorns.flyaware.data.remote.dto.AirportInfoDto
import com.defenseunicorns.flyaware.data.remote.dto.MetarDto
import com.defenseunicorns.flyaware.data.remote.dto.TafDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSourceImpl @Inject constructor(
    private val client: HttpClient
) : RemoteDataSource {

    companion object {
        private const val TAG = "RemoteDataSource"
        private const val BASE_URL = "https://aviationweather.gov/api/data"
    }

    override suspend fun getMetars(icaoCodes: List<String>): Result<List<MetarDto>> {
        return try {
            if (icaoCodes.isEmpty()) {
                return Result.success(emptyList())
            }
            
            val url = "$BASE_URL/metar?ids=${icaoCodes.joinToString(",")}&format=json"
            Log.d(TAG, "Fetching METARs for airports: $icaoCodes")
            
            val response = client.get(url)
            val metars: List<MetarDto> = response.body()
            
            Log.d(TAG, "Successfully fetched ${metars.size} METARs")
            Result.success(metars)
            
        } catch (e: ClientRequestException) {
            Log.e(TAG, "Client error fetching METARs: ${e.message}")
            Result.failure(e)
        } catch (e: ServerResponseException) {
            Log.e(TAG, "Server error fetching METARs: ${e.message}")
            Result.failure(e)
        } catch (e: RedirectResponseException) {
            Log.e(TAG, "Redirect error fetching METARs: ${e.message}")
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error fetching METARs: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getTafs(icaoCodes: List<String>): Result<List<TafDto>> {
        return try {
            if (icaoCodes.isEmpty()) {
                return Result.success(emptyList())
            }
            
            val url = "$BASE_URL/taf?ids=${icaoCodes.joinToString(",")}&format=json"
            Log.d(TAG, "Fetching TAFs for airports: $icaoCodes")
            Log.d(TAG, "TAF URL: $url")
            
            val response = client.get(url)
            Log.d(TAG, "TAF response status: ${response.status}")
            
            val tafs: List<TafDto> = response.body()
            
            Log.d(TAG, "Successfully fetched ${tafs.size} TAFs")
            
            // Debug: Log the first TAF to see its structure
            if (tafs.isNotEmpty()) {
                val firstTaf = tafs.first()
                Log.d(TAG, "First TAF structure: icaoId=${firstTaf.icaoId}, rawTAF=${firstTaf.rawTAF?.take(50)}")
                Log.d(TAG, "First TAF issueTime=${firstTaf.issueTime}, validTimeFrom=${firstTaf.validTimeFrom}, validTimeTo=${firstTaf.validTimeTo}")
                Log.d(TAG, "First TAF fcsts count: ${firstTaf.fcsts?.size}")
            } else {
                Log.w(TAG, "No TAFs returned from API - this might be normal if no TAFs are available")
            }
            
            // Temporarily return all TAFs without filtering to see what we get
            Result.success(tafs)
            
        } catch (e: ClientRequestException) {
            Log.e(TAG, "Client error fetching TAFs: ${e.message}")
            Result.failure(e)
        } catch (e: ServerResponseException) {
            Log.e(TAG, "Server error fetching TAFs: ${e.message}")
            Result.failure(e)
        } catch (e: RedirectResponseException) {
            Log.e(TAG, "Redirect error fetching TAFs: ${e.message}")
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error fetching TAFs: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getAirportInfo(icaoCode: String): Result<AirportInfoDto> {
        return try {
            val url = "$BASE_URL/airport?ids=$icaoCode&format=json"
            Log.d(TAG, "Fetching airport info for: $icaoCode")
            
            val response = client.get(url)
            val airports: List<AirportInfoDto> = response.body()
            
            if (airports.isNotEmpty()) {
                Log.d(TAG, "Successfully fetched airport info for: $icaoCode")
                Result.success(airports.first())
            } else {
                Log.w(TAG, "No airport info found for: $icaoCode")
                Result.failure(Exception("Airport not found: $icaoCode"))
            }
            
        } catch (e: ClientRequestException) {
            Log.e(TAG, "Client error fetching airport info: ${e.message}")
            Result.failure(e)
        } catch (e: ServerResponseException) {
            Log.e(TAG, "Server error fetching airport info: ${e.message}")
            Result.failure(e)
        } catch (e: RedirectResponseException) {
            Log.e(TAG, "Redirect error fetching airport info: ${e.message}")
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error fetching airport info: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun validateAirportCode(icaoCode: String): Result<Boolean> {
        return try {
            val url = "$BASE_URL/airport?ids=$icaoCode&format=json"
            Log.d(TAG, "Validating airport code: $icaoCode")
            
            val response = client.get(url)
            val airports: List<AirportInfoDto> = response.body()
            
            val isValid = airports.isNotEmpty()
            Log.d(TAG, "Airport code validation result for $icaoCode: $isValid")
            Result.success(isValid)
            
        } catch (e: ClientRequestException) {
            Log.e(TAG, "Client error validating airport code: ${e.message}")
            Result.failure(e)
        } catch (e: ServerResponseException) {
            Log.e(TAG, "Server error validating airport code: ${e.message}")
            Result.failure(e)
        } catch (e: RedirectResponseException) {
            Log.e(TAG, "Redirect error validating airport code: ${e.message}")
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error validating airport code: ${e.message}")
            Result.failure(e)
        }
    }
} 
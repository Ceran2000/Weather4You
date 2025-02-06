package pl.ceranka.weather4you.data.remote.repository

import android.util.Log
import kotlinx.coroutines.delay
import pl.ceranka.weather4you.domain.model.city.City
import pl.ceranka.weather4you.data.remote.api.OpenWeatherService
import pl.ceranka.weather4you.data.remote.model.city.asDomain
import retrofit2.await
import javax.inject.Inject

class CityRemoteRepository @Inject constructor(private val service: OpenWeatherService) {

    suspend fun loadCities(cityName: String): List<City> {
        return try {
            delay(300)
            service.getCities(cityName).await().list.map { it.asDomain() }
        } catch (e: Exception) {
            Log.e(TAG, "Error when fetching cities for $cityName", e)
            throw e
        }
    }

    companion object {
        private const val TAG = "CityRepository"
    }

}
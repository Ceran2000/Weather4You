package pl.ceranka.weather4you.data.remote.repository

import android.util.Log
import pl.ceranka.weather4you.domain.model.city.City
import pl.ceranka.weather4you.data.remote.api.OpenWeatherService
import pl.ceranka.weather4you.data.remote.model.city.asDomain
import retrofit2.await
import javax.inject.Inject

class CityRemoteRepository @Inject constructor(private val service: OpenWeatherService) {

    suspend fun loadCities(cityName: String): List<City> {
        return try {
            service.getCities(cityName).await().list.map { it.asDomain() }
        } catch (e: Exception) {
            Log.e(TAG, "Error when fetching cities for $cityName", e)
            throw e
        }
    }

    suspend fun getCityNameByCoordinates(latitude: Double, longitude: Double): String {
        return try {
            val weather = service.getWeatherByCoordinates(latitude, longitude)
            weather.name
        } catch (e: Exception) {
            Log.e(TAG, "Error when fetching city by coordinates ($latitude, $longitude)", e)
            throw e
        }
    }

    companion object {
        private const val TAG = "CityRepository"
    }

}
package pl.ceranka.weather4you.data.remote.repository

import android.util.Log
import pl.ceranka.weather4you.data.model.city.City
import pl.ceranka.weather4you.data.remote.api.OpenWeatherService
import pl.ceranka.weather4you.data.remote.model.city.asExternalModel
import retrofit2.await
import javax.inject.Inject

class CityRemoteRepository @Inject constructor(private val service: OpenWeatherService) {

    suspend fun loadCities(cityName: String): List<City> {
        return try {
            service.getCities(cityName).await().list.map { it.asExternalModel() }
        } catch (e: Exception) {
            Log.e(TAG, "Error when fetching cities for $cityName", e)
            throw e
        }
    }

    companion object {
        private const val TAG = "CityRepository"
    }

}
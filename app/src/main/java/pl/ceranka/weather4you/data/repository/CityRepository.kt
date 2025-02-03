package pl.ceranka.weather4you.data.repository

import android.util.Log
import pl.ceranka.weather4you.data.model.city.City
import pl.ceranka.weather4you.data.remote.OpenWeatherService
import retrofit2.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CityRepository @Inject constructor(private val service: OpenWeatherService) {

    suspend fun loadCities(cityName: String): List<City> {
        return try {
            service.getCities(cityName).await().list
        } catch (e: Exception) {
            Log.e(TAG, "Error when fetching cities for $cityName", e)
            emptyList()
        }
    }

    companion object {
        private const val TAG = "CityRepository"
    }

}
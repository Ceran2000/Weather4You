package pl.ceranka.weather4you.data.repository

import android.util.Log
import pl.ceranka.weather4you.data.model.weather.Weather
import pl.ceranka.weather4you.data.remote.api.OpenWeatherService
import pl.ceranka.weather4you.data.remote.model.weather.asExternalModel
import retrofit2.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(private val service: OpenWeatherService) {

    suspend fun loadCurrentWeatherForCity(id: Int): Weather = try {
        service.getWeather(id).await().asExternalModel()
    } catch (e: Exception) {
        Log.e(TAG, "Error when fetching weather for cityId $id", e)
        throw e
    }

    companion object {
        private const val TAG = "WeatherRepository"
    }
}
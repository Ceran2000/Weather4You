package pl.ceranka.weather4you.data.repository

import android.util.Log
import pl.ceranka.weather4you.data.model.forecast.Forecast
import pl.ceranka.weather4you.data.remote.model.forecast.ForecastResponse
import pl.ceranka.weather4you.data.remote.OpenWeatherService
import pl.ceranka.weather4you.data.remote.model.forecast.asExternalModel
import pl.ceranka.weather4you.data.repository.CityRepository.Companion
import retrofit2.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForecastRepository @Inject constructor(private val service: OpenWeatherService) {

    suspend fun loadForecastForCity(cityId: Int): List<Forecast> = try {
        service.getForecast(cityId).await().list.map { it.asExternalModel() }
    } catch (e: Exception) {
        Log.e(TAG, "Error when fetching forecast for cityId $cityId", e)
        throw e
    }

    companion object {
        private const val TAG = "ForecastRepository"
    }
}
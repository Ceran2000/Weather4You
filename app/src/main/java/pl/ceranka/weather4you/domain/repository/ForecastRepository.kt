package pl.ceranka.weather4you.domain.repository

import android.util.Log
import pl.ceranka.weather4you.domain.model.forecast.Forecast
import pl.ceranka.weather4you.data.remote.api.OpenWeatherService
import pl.ceranka.weather4you.data.remote.model.forecast.asDomain
import retrofit2.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForecastRepository @Inject constructor(private val service: OpenWeatherService) {

    suspend fun loadForecastForCity(cityId: Int): List<Forecast> = try {
        service.getForecast(cityId).await().list.map { it.asDomain() }
    } catch (e: Exception) {
        Log.e(TAG, "Error when fetching forecast for cityId $cityId", e)
        throw e
    }

    companion object {
        private const val TAG = "ForecastRepository"
    }
}
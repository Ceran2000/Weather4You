package pl.ceranka.weather4you.data.repository

import pl.ceranka.weather4you.data.model.forecast.ForecastResponse
import pl.ceranka.weather4you.data.remote.OpenWeatherService
import retrofit2.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForecastRepository @Inject constructor(private val service: OpenWeatherService) {

    //TODO: error handling
    suspend fun loadForecastForCity(cityId: Int): ForecastResponse =
        service.getForecast(cityId).await()
}
package pl.ceranka.weather4you.data.repository

import pl.ceranka.weather4you.data.model.weather.WeatherResponse
import pl.ceranka.weather4you.data.remote.OpenWeatherService
import retrofit2.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(private val service: OpenWeatherService) {

    //TODO: error handling
    suspend fun loadWeatherForCity(id: Int): WeatherResponse {
        return service.getWeather(id).await()
    }
}
package pl.ceranka.weather4you.data.repository

import pl.ceranka.weather4you.data.model.WeatherResponse
import pl.ceranka.weather4you.data.remote.OpenWeatherService
import retrofit2.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(private val service: OpenWeatherService) {

    suspend fun loadWeatherForCityId(id: Int): WeatherResponse {
        return service.getWeather(id).await()
    }
}
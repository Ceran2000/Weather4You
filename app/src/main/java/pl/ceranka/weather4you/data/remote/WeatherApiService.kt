package pl.ceranka.weather4you.data.remote

import pl.ceranka.weather4you.BuildConfig
import pl.ceranka.weather4you.data.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String = BuildConfig.API_KEY,
        @Query("units") units: String = "metric"
    ): Response<WeatherResponse>
}
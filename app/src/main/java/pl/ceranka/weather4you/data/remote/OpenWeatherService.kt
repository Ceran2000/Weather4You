package pl.ceranka.weather4you.data.remote

import pl.ceranka.weather4you.BuildConfig
import pl.ceranka.weather4you.data.model.CityResponse
import pl.ceranka.weather4you.data.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherService {
    @GET("weather")
    fun getWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String = BuildConfig.OPEN_WEATHER_API_KEY,
        @Query("units") units: String = "metric"
    ): Call<WeatherResponse>

    @GET("find")
    fun getCities(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String = BuildConfig.OPEN_WEATHER_API_KEY,
    ): Call<CityResponse>
}
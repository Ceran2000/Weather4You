package pl.ceranka.weather4you.data.remote.api

import pl.ceranka.weather4you.BuildConfig
import pl.ceranka.weather4you.data.remote.model.city.CityResponse
import pl.ceranka.weather4you.data.remote.model.forecast.ForecastResponse
import pl.ceranka.weather4you.data.remote.model.weather.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
//TODO: language
interface OpenWeatherService {

    @GET("find")
    fun getCities(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String = BuildConfig.OPEN_WEATHER_API_KEY,
    ): Call<CityResponse>

    @GET("weather")
    fun getWeather(
        @Query("id") cityId: Int,
        @Query("appid") apiKey: String = BuildConfig.OPEN_WEATHER_API_KEY,
        @Query("units") units: String = UNITS_METRIC
    ): Call<WeatherResponse>

    @GET("forecast")
    fun getForecast(
        @Query("id") cityId: Int,
        @Query("appid") apiKey: String = BuildConfig.OPEN_WEATHER_API_KEY,
        @Query("units") units: String = UNITS_METRIC
    ): Call<ForecastResponse>

    companion object {
        private const val UNITS_METRIC = "metric"
    }
}
package pl.ceranka.weather4you.data.remote.api

import androidx.compose.ui.text.intl.Locale
import pl.ceranka.weather4you.BuildConfig
import pl.ceranka.weather4you.data.remote.model.city.CityResponse
import pl.ceranka.weather4you.data.remote.model.forecast.ForecastResponse
import pl.ceranka.weather4you.data.remote.model.weather.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherService {

    @GET("find")
    fun getCities(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String = BuildConfig.OPEN_WEATHER_API_KEY,
        @Query("lang") lang: String = Locale.current.language
    ): Call<CityResponse>

    @GET("weather")
    fun getWeather(
        @Query("id") cityId: Int,
        @Query("appid") apiKey: String = BuildConfig.OPEN_WEATHER_API_KEY,
        @Query("units") units: String = UNITS_METRIC,
        @Query("lang") lang: String = Locale.current.language
    ): Call<WeatherResponse>

    @GET("forecast")
    fun getForecast(
        @Query("id") cityId: Int,
        @Query("appid") apiKey: String = BuildConfig.OPEN_WEATHER_API_KEY,
        @Query("units") units: String = UNITS_METRIC,
        @Query("lang") lang: String = Locale.current.language
    ): Call<ForecastResponse>

    @GET("weather")
    suspend fun getWeatherByCoordinates(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String = BuildConfig.OPEN_WEATHER_API_KEY,
        @Query("units") units: String = UNITS_METRIC,
        @Query("lang") lang: String = Locale.current.language
    ): WeatherResponse

    companion object {
        private const val UNITS_METRIC = "metric"
    }
}
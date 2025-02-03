package pl.ceranka.weather4you.data.remote.model.weather

import pl.ceranka.weather4you.data.model.weather.Weather as WeatherExternalModel
import pl.ceranka.weather4you.data.remote.model.forecast.Sys
import pl.ceranka.weather4you.data.remote.model.forecast.Weather
import pl.ceranka.weather4you.data.remote.model.forecast.Wind
import kotlin.math.roundToInt

data class WeatherResponse(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)

data class Clouds(
    val all: Int
)

data class Coord(
    val lat: Double,
    val lon: Double
)

data class Main(
    val feels_like: Double,
    val grnd_level: Int,
    val humidity: Int,
    val pressure: Int,
    val sea_level: Int,
    val temp: Double,
    val temp_max: Double,
    val temp_min: Double
)

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)

data class Wind(
    val deg: Int,
    val gust: Double,
    val speed: Double
)

fun WeatherResponse.asExternalModel(): WeatherExternalModel {
    val weather = weather.first()   //TODO

    return WeatherExternalModel(
        name = name,
        description = weather.description,
        iconCode = weather.icon,
        temp = main.temp.roundToInt(),
        tempFeelsLike = main.feels_like.roundToInt(),
        humidity = main.humidity,
        cloudinessInPercentage = clouds.all,
        visibilityInMeters = visibility
    )
}
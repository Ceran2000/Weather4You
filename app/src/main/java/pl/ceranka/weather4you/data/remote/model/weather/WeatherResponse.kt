package pl.ceranka.weather4you.data.remote.model.weather

import pl.ceranka.weather4you.domain.model.Icon
import pl.ceranka.weather4you.domain.model.Temperature
import pl.ceranka.weather4you.domain.model.weather.Weather as WeatherDomainModel
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

data class Sys(
    val country: String,
    val id: Int,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
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

fun WeatherResponse.asDomain(): WeatherDomainModel {
    val weather = weather.first()

    return WeatherDomainModel(
        cityName = name,
        description = weather.description,
        icon = Icon(weather.icon),
        temp = Temperature(main.temp.roundToInt()),
        tempFeelsLike = Temperature(main.feels_like.roundToInt()),
        humidity = main.humidity,
        cloudinessInPercentage = clouds.all,
        visibilityInMeters = visibility
    )
}
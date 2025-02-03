package pl.ceranka.weather4you.data.remote.model.forecast

import pl.ceranka.weather4you.data.model.Icon
import pl.ceranka.weather4you.data.model.Temperature
import kotlin.math.roundToInt
import pl.ceranka.weather4you.data.model.forecast.Forecast as ForecastExternalModel

data class ForecastResponse(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Forecast>,
    val message: Int
)

data class City(
    val coord: Coord,
    val country: String,
    val id: Int,
    val name: String,
    val population: Int,
    val sunrise: Int,
    val sunset: Int,
    val timezone: Int
)

data class Forecast(
    val clouds: Clouds,
    val dt: Int,
    val dt_txt: String,
    val main: Main,
    val pop: Double,
    val rain: Rain,
    val snow: Snow,
    val sys: Sys,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)

data class Coord(
    val lat: Double,
    val lon: Double
)

data class Clouds(
    val all: Int
)

data class Main(
    val feels_like: Double,
    val grnd_level: Int,
    val humidity: Int,
    val pressure: Int,
    val sea_level: Int,
    val temp: Double,
    val temp_kf: Double,
    val temp_max: Double,
    val temp_min: Double
)

data class Rain(
    val `3h`: Double
)

data class Snow(
    val `3h`: Double
)

data class Sys(
    val pod: String
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


fun Forecast.asExternalModel() = ForecastExternalModel(
    dateTimeSecondsUTC = dt,
    icon = Icon(weather.first().icon),            //TODO
    temp = Temperature(main.temp.roundToInt()),
    precipitationInPercentage = pop.roundToInt()
)
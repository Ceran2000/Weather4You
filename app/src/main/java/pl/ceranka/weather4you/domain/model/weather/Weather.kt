package pl.ceranka.weather4you.domain.model.weather

import pl.ceranka.weather4you.domain.model.Icon
import pl.ceranka.weather4you.domain.model.Temperature

data class Weather(
    val cityName: String,
    val description: String,
    val icon: Icon,
    val temp: Temperature,
    val tempFeelsLike: Temperature,
    val humidity: Int,
    val cloudinessInPercentage: Int,
    val visibilityInMeters: Int
)
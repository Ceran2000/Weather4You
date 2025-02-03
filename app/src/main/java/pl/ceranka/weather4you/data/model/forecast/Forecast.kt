package pl.ceranka.weather4you.data.model.forecast

import pl.ceranka.weather4you.data.model.Icon
import pl.ceranka.weather4you.data.model.Temperature

data class Forecast(
    val dateTimeSecondsUTC: Int,
    val icon: Icon,
    val temp: Temperature,
    val precipitationInPercentage: Int
)
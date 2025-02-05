package pl.ceranka.weather4you.domain.model.forecast

import pl.ceranka.weather4you.domain.model.Icon
import pl.ceranka.weather4you.domain.model.Temperature

data class Forecast(
    val dateTimeSecondsUTC: Int,
    val icon: Icon,
    val temp: Temperature,
    val precipitationInPercentage: Int
)
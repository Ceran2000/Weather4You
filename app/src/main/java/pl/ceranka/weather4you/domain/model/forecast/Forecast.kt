package pl.ceranka.weather4you.domain.model.forecast

import pl.ceranka.weather4you.domain.model.Icon
import pl.ceranka.weather4you.domain.model.Temperature
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

data class Forecast(
    private val dateTimeSecondsUTC: Int,
    val icon: Icon,
    val temp: Temperature,
    val precipitationInPercentage: Int
) {
    private val dateTime: ZonedDateTime by lazy {
        Instant.ofEpochSecond(dateTimeSecondsUTC.toLong()).atZone(ZoneId.systemDefault())
    }

    val dayOfWeekUiValue: String by lazy {
        dateTime.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    }

    val timeUiValue: String by lazy {
        dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    }
}
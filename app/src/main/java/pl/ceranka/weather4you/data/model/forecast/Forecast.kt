package pl.ceranka.weather4you.data.model.forecast

data class Forecast(
    val dateTimeSecondsUTC: Int,
    val iconCode: String,
    val temp: Int,
    val precipitationInPercentage: Int
)
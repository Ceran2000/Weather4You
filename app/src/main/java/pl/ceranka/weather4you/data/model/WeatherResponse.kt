package pl.ceranka.weather4you.data.model

data class WeatherResponse(
    val main: Main
)

data class Main(
    val temp: Double
)
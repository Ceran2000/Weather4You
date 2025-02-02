package pl.ceranka.weather4you.data.model

data class Coord(
    val lat: Double,
    val lon: Double
)

data class Sys(
    val country: String,
    val id: Int,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
)
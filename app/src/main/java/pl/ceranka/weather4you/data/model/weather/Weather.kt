package pl.ceranka.weather4you.data.model.weather

data class Weather(
    val name: String,       //TODO: rename to cityName
    val description: String,
    val iconCode: String,
    val temp: Int,
    val tempFeelsLike: Int,
    val humidity: Int,
    val cloudinessInPercentage: Int,
    val visibilityInMeters: Int
)
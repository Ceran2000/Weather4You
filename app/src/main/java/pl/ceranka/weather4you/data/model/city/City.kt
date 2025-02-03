package pl.ceranka.weather4you.data.model.city

data class City(
    val id: Int,
    val name: String,
    val country: String,
    val coord: Coord
) {
    val title: String by lazy { "$name, $country" }
    val subTitle: String by lazy { coord.toString() }
}

data class Coord(
    val lat: Double,
    val lon: Double
) {
    override fun toString(): String = "[$lat, $lon]"
}
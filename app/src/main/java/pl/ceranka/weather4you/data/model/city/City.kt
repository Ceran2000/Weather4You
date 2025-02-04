package pl.ceranka.weather4you.data.model.city

import pl.ceranka.weather4you.data.local.model.CityEntity
import pl.ceranka.weather4you.data.local.model.CoordEntity

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

fun City.asEntity() = CityEntity(
    id = id,
    name = name,
    country = country,
    coord = CoordEntity(coord.lat, coord.lon)
)
package pl.ceranka.weather4you.data.remote.model.city

import pl.ceranka.weather4you.domain.model.city.City as CityDomainModel
import pl.ceranka.weather4you.domain.model.city.Coord as CoordDomainModel

data class CityResponse(
    val cod: String,
    val count: Int,
    val list: List<City>,
    val message: String
)

data class City(
    val coord: Coord,
    val dt: Int,
    val id: Int,
    val name: String,
    val sys: Sys
)

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

fun City.asDomain() = CityDomainModel(id = id, name = name, country = sys.country, coord = CoordDomainModel(coord.lat, coord.lon))
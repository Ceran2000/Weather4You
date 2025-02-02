package pl.ceranka.weather4you.data.model

data class CityResponse(
    val cod: String,
    val count: Int,
    val list: List<City>,
    val message: String
)

data class City(
    val coord: Coord,
    val dt: Int,        //TODO: whats that?
    val id: Int,
    val name: String,
    val sys: Sys
)

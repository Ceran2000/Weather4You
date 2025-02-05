package pl.ceranka.weather4you.data.local.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.ceranka.weather4you.domain.model.city.City
import pl.ceranka.weather4you.domain.model.city.Coord

@Entity(tableName = "city_history")
data class CityEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val country: String,
    @Embedded val coord: CoordEntity,
    val timestamp: Long = System.currentTimeMillis()
)

data class CoordEntity(val lat: Double, val lon: Double)

fun CityEntity.asDomain(): City = City(
    id = id,
    name = name,
    country = country,
    coord = Coord(coord.lat, coord.lon)
)
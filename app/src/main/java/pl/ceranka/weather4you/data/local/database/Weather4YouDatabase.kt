package pl.ceranka.weather4you.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.ceranka.weather4you.data.local.dao.CityDao
import pl.ceranka.weather4you.data.local.model.CityEntity

@Database(entities = [CityEntity::class], version = 1, exportSchema = false)
abstract class Weather4YouDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
}
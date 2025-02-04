package pl.ceranka.weather4you.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pl.ceranka.weather4you.data.local.model.CityEntity

@Dao
interface CityDao {

    @Query("SELECT * FROM city_history ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentCities(limit: Int): Flow<List<CityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: CityEntity)

    @Query("DELETE FROM city_history WHERE id = :cityId")
    suspend fun deleteCity(cityId: Int): Int

    @Query("DELETE FROM city_history")
    suspend fun clearCities(): Int
}
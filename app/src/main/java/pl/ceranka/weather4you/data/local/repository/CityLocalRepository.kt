package pl.ceranka.weather4you.data.local.repository

import kotlinx.coroutines.flow.Flow
import pl.ceranka.weather4you.data.local.dao.CityDao
import pl.ceranka.weather4you.data.local.model.CityEntity
import javax.inject.Inject

class CityLocalRepository @Inject constructor(private val cityDao: CityDao) {

    fun recentCities(limit: Int): Flow<List<CityEntity>> = cityDao.getRecentCities(limit)

    suspend fun addCity(city: CityEntity) = cityDao.insertCity(city)

    suspend fun deleteCity(id: Int) = cityDao.deleteCity(id)

    suspend fun clearCities() = cityDao.clearCities()

}
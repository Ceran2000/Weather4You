package pl.ceranka.weather4you.data.local.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.ceranka.weather4you.data.local.dao.CityDao
import pl.ceranka.weather4you.data.local.model.asExternalModel
import pl.ceranka.weather4you.data.model.city.City
import pl.ceranka.weather4you.data.model.city.asEntity
import javax.inject.Inject

class CityLocalRepository @Inject constructor(private val cityDao: CityDao) {

    fun recentCities(limit: Int): Flow<List<City>> = cityDao.getRecentCities(limit)
        .map { cities -> cities.map { city -> city.asExternalModel() } }

    suspend fun addCity(city: City) = cityDao.insertCity(city.asEntity())

    suspend fun deleteCity(id: Int) = cityDao.deleteCity(id)

    suspend fun clearCities() = cityDao.clearCities()

}
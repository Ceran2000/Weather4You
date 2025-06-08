package pl.ceranka.weather4you.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.ceranka.weather4you.data.local.model.asDomain
import pl.ceranka.weather4you.data.local.repository.CityLocalRepository
import pl.ceranka.weather4you.data.remote.model.city.asDomain
import pl.ceranka.weather4you.domain.model.city.City
import pl.ceranka.weather4you.data.remote.repository.CityRemoteRepository
import pl.ceranka.weather4you.domain.model.city.asEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CityRepository @Inject constructor(private val localRepository: CityLocalRepository, private val remoteRepository: CityRemoteRepository) {

    suspend fun searchCities(cityNameQuery: String): List<City> =
        remoteRepository.loadCities(cityName = cityNameQuery).map { it.asDomain() }

    fun citiesHistory(limit: Int): Flow<List<City>> = localRepository.recentCities(limit).map { cities -> cities.map { it.asDomain() } }

    suspend fun addCityToHistory(city: City) = localRepository.addCity(city.asEntity())

    suspend fun deleteCityFromHistory(id: Int) = localRepository.deleteCity(id)

    suspend fun clearHistory() = localRepository.clearCities()

    suspend fun getCityNameByCoordinates(lat: Double, lon: Double): City = remoteRepository.getCityNameByCoordinates(lat, lon).asDomain()
}
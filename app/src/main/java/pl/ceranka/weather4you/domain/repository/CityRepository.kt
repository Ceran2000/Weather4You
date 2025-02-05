package pl.ceranka.weather4you.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.ceranka.weather4you.data.local.repository.CityLocalRepository
import pl.ceranka.weather4you.domain.model.city.City
import pl.ceranka.weather4you.data.remote.repository.CityRemoteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CityRepository @Inject constructor(private val localRepository: CityLocalRepository, private val remoteRepository: CityRemoteRepository) {

    suspend fun searchCities(cityNameQuery: String): List<City> =
        remoteRepository.loadCities(cityName = cityNameQuery)

    fun citiesHistory(limit: Int): Flow<List<City>> = localRepository.recentCities(limit)

    suspend fun addCityToHistory(city: City) = localRepository.addCity(city)

    suspend fun deleteCityFromHistory(id: Int) = localRepository.deleteCity(id)

    suspend fun clearHistory() = localRepository.clearCities()
}
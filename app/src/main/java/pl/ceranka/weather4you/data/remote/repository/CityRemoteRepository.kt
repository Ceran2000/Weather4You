package pl.ceranka.weather4you.data.remote.repository

import android.util.Log
import pl.ceranka.weather4you.data.remote.api.OpenWeatherService
import pl.ceranka.weather4you.data.remote.model.city.City
import pl.ceranka.weather4you.data.remote.model.city.Coord
import pl.ceranka.weather4you.data.remote.model.city.Sys
import retrofit2.await
import javax.inject.Inject

class CityRemoteRepository @Inject constructor(private val service: OpenWeatherService) {

    suspend fun loadCities(cityName: String): List<City> {
        return try {
            service.getCities(cityName).await().list
        } catch (e: Exception) {
            Log.e(TAG, "Error when fetching cities for $cityName", e)
            throw e
        }
    }

    suspend fun getCityNameByCoordinates(latitude: Double, longitude: Double): City {
         try {
            val weather = service.getWeatherByCoordinates(latitude, longitude)
            return weather.let {
                City(
                    id = it.id,
                    name = it.name,
                    coord = Coord(it.coord.lat, it.coord.lon),
                    sys = Sys(it.sys.country, it.sys.id, it.sys.sunrise, it.sys.sunset, it.sys.type),
                    dt = it.dt
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error when fetching city by coordinates ($latitude, $longitude)", e)
            throw e
        }
    }

    companion object {
        private const val TAG = "CityRepository"
    }

}
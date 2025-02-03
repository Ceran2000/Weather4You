package pl.ceranka.weather4you.ui.weather_for_city

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.ceranka.weather4you.data.model.forecast.Forecast
import pl.ceranka.weather4you.data.model.weather.Weather
import pl.ceranka.weather4you.data.repository.ForecastRepository
import pl.ceranka.weather4you.data.repository.WeatherRepository
import javax.inject.Inject

@HiltViewModel
class WeatherForCityViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val forecastRepository: ForecastRepository,
    savedStateHandle: SavedStateHandle,
    application: Application
) : AndroidViewModel(application) {

    private val cityId: Int = checkNotNull(savedStateHandle["id"])

    private val _weather = MutableStateFlow<Weather?>(null)
    val weather = _weather.asStateFlow()

    private val _forecasts = MutableStateFlow<List<Forecast>?>(null)
    val forecasts = _forecasts.asStateFlow()

    init {
        viewModelScope.launch {
            //TODO: async/await?
            _weather.update { weatherRepository.loadCurrentWeatherForCity(cityId) }
            _forecasts.update { forecastRepository.loadForecastForCity(cityId) }
        }
    }

}
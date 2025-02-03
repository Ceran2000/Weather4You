package pl.ceranka.weather4you.ui.weather_for_city

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _weatherUiState = MutableStateFlow<UiState<Weather>>(UiState.Loading)
    val weatherUiState = _weatherUiState.asStateFlow()

    fun onRefreshClicked() {
        viewModelScope.launch {
            loadWeather()
        }
    }

    private suspend fun loadWeather() {
        try {
            _weatherUiState.emit(UiState.Loading)
            val weather = weatherRepository.loadCurrentWeatherForCity(cityId)
            _weatherUiState.emit(UiState.ShowContent(weather))
        } catch (e: Exception) {
            _weatherUiState.emit(UiState.Error("Unknown error"))
        }
    }

    private val _forecasts = MutableStateFlow<List<Forecast>?>(null)
    val forecasts = _forecasts.asStateFlow()

    private suspend fun loadForecasts() {
        _forecasts.value = forecastRepository.loadForecastForCity(cityId)
    }

    init {
        viewModelScope.launch {
            val weatherDeferred = async { loadWeather() }
            val forecastDeferred = async { loadForecasts() }

            weatherDeferred.await()
            forecastDeferred.await()
        }
    }

}
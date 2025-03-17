package pl.ceranka.weather4you.ui.weather_for_city

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import pl.ceranka.weather4you.R
import pl.ceranka.weather4you.domain.model.forecast.Forecast
import pl.ceranka.weather4you.domain.model.weather.Weather
import pl.ceranka.weather4you.domain.repository.ForecastRepository
import pl.ceranka.weather4you.domain.repository.WeatherRepository
import pl.ceranka.weather4you.ui.base.BaseViewModel
import pl.ceranka.weather4you.ui.util.UiText
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class WeatherForCityViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val forecastRepository: ForecastRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val cityId: Int = checkNotNull(savedStateHandle["id"])

    private val _weatherUiState = MutableStateFlow<UiState<Weather>>(UiState.Loading)
    val weatherUiState = _weatherUiState.asStateFlow()

    fun onRefreshClicked() {
        viewModelScope.launch {
            loadWeather()
        }
    }

    private suspend fun loadWeather() = flow<UiState<Weather>> {
        val weather = weatherRepository.loadCurrentWeatherForCity(cityId)
        emit(UiState.ShowContent(weather))
    }
        .onStart { emit(UiState.Loading) }
        .catch { e ->
            if (e !is CancellationException) {
                val errorState = UiState.Error(UiText.StringResource(R.string.unknown_error_message))
                emit(errorState)
            } else {
                throw e // Re-throw CancellationException for proper coroutine cancellation
            }
        }
        .run { _weatherUiState.emitAll(this) }

    private val _forecasts = MutableStateFlow<List<Forecast>?>(null)
    val forecasts = _forecasts.asStateFlow()

    private suspend fun loadForecasts() {
        val data = try {
            forecastRepository.loadForecastForCity(cityId)
        } catch (e: Exception) {
            null
        }
        _forecasts.value = data
    }

    init {
        launchWithErrorHandling {
            val weatherDeferred = async { loadWeather() }
            val forecastDeferred = async { loadForecasts() }

            weatherDeferred.await()
            forecastDeferred.await()
        }
    }

}
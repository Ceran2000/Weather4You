package pl.ceranka.weather4you.ui.weather_for_city

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.ceranka.weather4you.domain.model.forecast.Forecast
import pl.ceranka.weather4you.domain.model.weather.Weather
import pl.ceranka.weather4you.domain.repository.ForecastRepository
import pl.ceranka.weather4you.domain.repository.WeatherRepository
import pl.ceranka.weather4you.util.MainDispatcherRule
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
class WeatherForCityViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private lateinit var weatherRepository: WeatherRepository
    private lateinit var forecastRepository: ForecastRepository
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: WeatherForCityViewModel

    private val cityId = 12345

    @Before
    fun setup() {
        weatherRepository = mockk()
        forecastRepository = mockk()
        savedStateHandle = SavedStateHandle().apply {
            set("id", cityId)
        }
    }

    @Test
    fun `init should load weather and forecasts`() = runTest {
        // Given
        val mockWeather = mockk<Weather>()
        val mockForecasts = listOf<Forecast>(mockk(), mockk())

        coEvery { weatherRepository.loadCurrentWeatherForCity(cityId) } returns mockWeather
        coEvery { forecastRepository.loadForecastForCity(cityId) } returns mockForecasts

        // When
        viewModel = WeatherForCityViewModel(weatherRepository, forecastRepository, savedStateHandle)
        advanceUntilIdle()

        // Then
        viewModel.weatherUiState.test {
            val item = awaitItem()
            assertTrue(item is UiState.ShowContent)
            assertEquals(mockWeather, (item as UiState.ShowContent).data)
        }

        viewModel.forecasts.test {
            val item = awaitItem()
            assertThat(item, notNullValue())
            assertEquals(mockForecasts, item)
        }
    }

    @Test
    fun `onRefreshClicked should reload weather data`() = runTest {
        // Given
        val mockWeather = mockk<Weather>()
        val mockForecasts = listOf<Forecast>(mockk())

        coEvery { weatherRepository.loadCurrentWeatherForCity(cityId) } returns mockWeather
        coEvery { forecastRepository.loadForecastForCity(cityId) } returns mockForecasts

        viewModel = WeatherForCityViewModel(weatherRepository, forecastRepository, savedStateHandle)
        advanceUntilIdle()

        // Clear initial emissions
        viewModel.weatherUiState.test {
            cancelAndIgnoreRemainingEvents()
        }

        // When
        viewModel.onRefreshClicked()
        advanceUntilIdle()

        // Then
        viewModel.weatherUiState.test {
            val item = awaitItem()
            assertTrue(item is UiState.ShowContent)
            assertEquals(mockWeather, (item as UiState.ShowContent).data)
        }
    }

    @Test
    fun `should handle weather loading error`() = runTest {
        // Given
        coEvery { weatherRepository.loadCurrentWeatherForCity(cityId) } throws Exception("Network error")

        // When
        viewModel = WeatherForCityViewModel(weatherRepository, forecastRepository, savedStateHandle)
        advanceUntilIdle()

        // Then
        viewModel.weatherUiState.test {
            val item = awaitItem()
            assertTrue(item is UiState.Error)
        }
    }

    @Test
    fun `should handle forecast loading error`() = runTest {
        // Given
        val mockWeather = mockk<Weather>()

        coEvery { weatherRepository.loadCurrentWeatherForCity(cityId) } returns mockWeather
        coEvery { forecastRepository.loadForecastForCity(cityId) } throws Exception("Network error")

        // When
        viewModel = WeatherForCityViewModel(weatherRepository, forecastRepository, savedStateHandle)
        advanceUntilIdle()

        // Then

        viewModel.forecasts.test {
            val forecasts = awaitItem()
            assertNull(forecasts)
        }
    }

    @Test
    fun `should properly handle CancellationException in loadWeather`() = runTest {
        // Given
        coEvery { weatherRepository.loadCurrentWeatherForCity(cityId) } throws CancellationException("Cancelled")
        coEvery { forecastRepository.loadForecastForCity(cityId) } returns emptyList()

        // When
        viewModel = WeatherForCityViewModel(weatherRepository, forecastRepository, savedStateHandle)
        advanceUntilIdle()

        viewModel.weatherUiState.test {
            val item = awaitItem()
            assertTrue(item is UiState.Loading)
            expectNoEvents()
        }
    }

}
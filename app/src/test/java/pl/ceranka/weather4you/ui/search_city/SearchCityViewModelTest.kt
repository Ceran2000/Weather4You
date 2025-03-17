package pl.ceranka.weather4you.ui.search_city

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Assert.assertEquals
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.ceranka.weather4you.domain.model.city.City
import pl.ceranka.weather4you.domain.model.city.Coord
import pl.ceranka.weather4you.domain.repository.CityRepository
import pl.ceranka.weather4you.util.MainDispatcherRule

@OptIn(ExperimentalCoroutinesApi::class)
class SearchCityViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private lateinit var viewModel: SearchCityViewModel
    private val cityRepository: CityRepository = mockk(relaxed = true)
    private val uiStateFactory: UiStateFactory = UiStateFactory()

    @Before
    fun setUp() {
        viewModel = SearchCityViewModel(cityRepository, uiStateFactory)
    }

    @Test
    fun `initial state should be UiState_Initial`() = runTest {
        assertEquals(UiState.Initial, viewModel.uiState.value)
    }

    @Test
    fun `searchCities should update uiState with results`() = runTest {
        // Given
        val city = City(1, "Warsaw", "PL", Coord(52.2298, 21.0118))
        coEvery { cityRepository.searchCities("Warsaw") } returns listOf(city)

        // When
        viewModel.onSearchQueryChanged("Warsaw")
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value, equalTo(UiState.ShowResults(listOf(city))))
    }

    @Test
    fun `searching for a non-existent city should emit Empty`() = runTest {
        // Given
        coEvery { cityRepository.searchCities("NonExistentCity") } returns emptyList()

        // When
        viewModel.onSearchQueryChanged("UnknownCity")
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value, equalTo(UiState.Empty))
    }

    @Test
    fun `onCityItemClicked should add city to history`() = runTest {
        // Given
        val city = City(2, "Berlin", "DE", Coord(52.52, 13.405))

        // When
        viewModel.onCityItemClicked(city)
        advanceUntilIdle()

        // Then
        coVerify { cityRepository.addCityToHistory(city) }
    }

    @Test
    fun `onCityItemClicked should handle exceptions`() = runTest {
        // Given
        val city = City(3, "Paris", "FR", Coord(48.8566, 2.3522))
        coEvery { cityRepository.addCityToHistory(city) } throws RuntimeException("DB error")

        // When
        viewModel.onCityItemClicked(city)
        advanceUntilIdle()

        // Then
        assertThat(viewModel.toastMessage, notNullValue())
    }
}
package pl.ceranka.weather4you.ui.search_city

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pl.ceranka.weather4you.R
import pl.ceranka.weather4you.domain.model.city.City
import pl.ceranka.weather4you.domain.repository.CityRepository
import pl.ceranka.weather4you.ui.base.BaseViewModel
import pl.ceranka.weather4you.ui.util.UiText
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@Suppress("OPT_IN_USAGE")
@HiltViewModel
class SearchCityViewModel @Inject constructor(
    private val cityRepository: CityRepository,
    private val uiStateFactory: UiStateFactory
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    fun onSearchQueryChanged(input: String) {
        _searchQuery.value = input
    }

    fun onClearSearchQueryClicked() {
        _searchQuery.value = ""
    }

    private val searchQueryRegex: Regex by lazy {
        "^(?!\\s*$)[A-Za-zĄĆĘŁŃÓŚŹŻąćęłńóśźż\\s-]+$".toRegex() //TODO: should ignore '-'?
    }

    val showSearchQueryError: StateFlow<Boolean> by lazy {
        searchQuery
            .map { it.isNotEmpty() && !it.matches(searchQueryRegex) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)
    }

    private suspend fun handleCitySearchQuery(cityName: String) {
        when {
            cityName.isEmpty() -> _uiState.emit(UiState.Initial)
            !cityName.matches(searchQueryRegex) -> _uiState.emit(UiState.Empty)
            else -> searchCities(cityName)
        }
    }

    private suspend fun searchCities(cityName: String) = flow {
        val cities = cityRepository.searchCities(cityName)
        val state = uiStateFactory.mapCitiesToUiState(cities)
        emit(state)
    }
        .onStart { emit(UiState.Loading) }
        .catch { e ->
            if (e !is CancellationException) {
                val errorState = UiState.Error(UiText.StringResource(R.string.unknown_error_message))
                emit(errorState)
            }
        }
        .run { _uiState.emitAll(this) }

    val recentCities: StateFlow<List<City>> by lazy {
        cityRepository.citiesHistory(limit = 5)
            .catch { emit(emptyList()) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    }

    fun onCityItemClicked(city: City) {
        launchWithErrorHandling {
            cityRepository.addCityToHistory(city)
        }
    }

    fun onRemoveRecentCityClicked(city: City) {
        launchWithErrorHandling {
            cityRepository.deleteCityFromHistory(city.id)
        }
    }

    val initialState: StateFlow<InitialState> by lazy {
        recentCities
            .map { list -> uiStateFactory.determineInitialState(list) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), InitialState.ANIMATION)
    }

    init {
        searchQuery
            .debounce(300)
            .onEach { query -> handleCitySearchQuery(query) }
            .launchIn(viewModelScope)
    }

}
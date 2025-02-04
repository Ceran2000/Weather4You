package pl.ceranka.weather4you.ui.search_city

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pl.ceranka.weather4you.data.model.city.City
import pl.ceranka.weather4you.data.repository.CityRepository
import javax.inject.Inject

@Suppress("OPT_IN_USAGE")
@HiltViewModel
class SearchCityViewModel @Inject constructor(
    application: Application,
    private val cityRepository: CityRepository
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow<UiState<List<City>>>(UiState.Initial)
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

    private suspend fun searchCities(cityName: String) {
        try {
            _uiState.value = UiState.Loading
            val cities = cityRepository.searchCities(cityName)
            _uiState.value = if (cities.isEmpty()) UiState.Empty else UiState.ShowContent(cities)
        } catch (e: Exception) {
            UiState.Error("Unknown error")
        }
    }

    val recentCities: StateFlow<List<City>> by lazy {
        cityRepository.citiesHistory(limit = 5)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    }

    fun onCityItemClicked(city: City) {
        viewModelScope.launch {
            cityRepository.addCityToHistory(city)
        }
    }

    fun onRemoveRecentCityClicked(city: City) {
        viewModelScope.launch {
            cityRepository.deleteCityFromHistory(city.id)
        }
    }

    fun onClearHistoryClicked() {
        viewModelScope.launch {
            cityRepository.clearHistory()
        }
    }

    init {
        //TODO: test
        searchQuery
            .debounce(300)
            .onEach { query -> handleCitySearchQuery(query) }
            .launchIn(viewModelScope)
    }

}
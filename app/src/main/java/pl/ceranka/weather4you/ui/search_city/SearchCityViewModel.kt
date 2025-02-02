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
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import pl.ceranka.weather4you.data.model.City
import pl.ceranka.weather4you.data.repository.CityRepository
import javax.inject.Inject

@Suppress("OPT_IN_USAGE")
@HiltViewModel
class SearchCityViewModel @Inject constructor(
    application: Application,
    private val cityRepository: CityRepository
) : AndroidViewModel(application) {

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

    private val _cities = MutableStateFlow<List<City>>(emptyList())
    val cities = _cities.asStateFlow()

    private suspend fun searchCityTemp(cityName: String): List<City> = cityRepository.loadCities(cityName)

    init {
        //TODO: test
        searchQuery
            .debounce(300)
            .onEach { query ->
                _cities.update {
                    if (query.matches(searchQueryRegex)) searchCityTemp(query) else emptyList()
                }
            }
            .launchIn(viewModelScope)
    }

}
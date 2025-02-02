package pl.ceranka.weather4you.ui.search_city

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.ceranka.weather4you.data.model.City
import pl.ceranka.weather4you.data.remote.OpenWeatherService
import retrofit2.await
import javax.inject.Inject

@HiltViewModel
class SearchCityViewModel @Inject constructor(
    application: Application,
    private val weatherService: OpenWeatherService
) : AndroidViewModel(application) {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    fun onSearchQueryChanged(input: String) {
        _searchQuery.value = input
    }

    fun clearSearchQueryClicked() {
        _searchQuery.value = ""
    }

    private val _cities = MutableStateFlow<List<City>>(emptyList())
    val cities = _cities.asStateFlow()

    private suspend fun searchCityTemp(cityName: String): List<City> {
        return try {
            weatherService.getCities(cityName).await().list
        } catch (e: Exception) {
            emptyList()
        }
    }

    init {
        searchQuery
            .drop(1)
            .debounce(300)
            .onEach { query ->
                //TODO: error handling
                _cities.update { searchCityTemp(query) }
            }
            .launchIn(viewModelScope)

    }

}
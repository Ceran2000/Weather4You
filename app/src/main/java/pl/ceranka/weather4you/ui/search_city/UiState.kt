package pl.ceranka.weather4you.ui.search_city

import pl.ceranka.weather4you.domain.model.city.City
import pl.ceranka.weather4you.ui.util.UiText

sealed class UiState {

    open val data: List<City>? = null
    open val errorMessage: UiText? = null

    data class ShowResults(override val data: List<City>) : UiState()

    data object Initial : UiState()

    data object Loading : UiState()

    data class Error(override val errorMessage: UiText) : UiState()

    data object Empty : UiState()
}
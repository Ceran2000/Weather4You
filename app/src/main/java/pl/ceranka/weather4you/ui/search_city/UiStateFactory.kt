package pl.ceranka.weather4you.ui.search_city

import pl.ceranka.weather4you.domain.model.city.City

class UiStateFactory {

    fun mapCitiesToUiState(cities: List<City>): UiState {
        return if (cities.isEmpty()) {
            UiState.Empty
        } else {
            UiState.ShowResults(cities)
        }
    }

    fun determineInitialState(recentCities: List<City>): InitialState {
        return if (recentCities.isEmpty()) {
            InitialState.ANIMATION
        } else {
            InitialState.HISTORY
        }
    }

}
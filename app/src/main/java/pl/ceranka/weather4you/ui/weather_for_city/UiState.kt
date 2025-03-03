package pl.ceranka.weather4you.ui.weather_for_city

import pl.ceranka.weather4you.ui.util.UiText


sealed class UiState<out T> {

    open val data: T? = null
    open val showContent = false
    open val showLoading = false
    open val errorMessage: UiText? = null
    open val showError = false

    data class ShowContent<T>(override val data: T) : UiState<T>() {
        override val showContent: Boolean = true
    }

    data object Loading : UiState<Nothing>() {
        override val showLoading = true
    }

    data class Error(override val errorMessage: UiText) : UiState<Nothing>() {
        override val showError = true
    }
}
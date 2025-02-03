package pl.ceranka.weather4you.ui.util

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class ShowContent<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
    data object Empty : UiState<Nothing>()
}
package pl.ceranka.weather4you.ui.search_city

sealed class UiState<out T> {

    open val data: T? = null
    open val showResults = false
    open val showHistory = false
    open val showLoading = false
    open val errorMessage: String? = null
    val showError get() = errorMessage != null
    open val showEmptyState = false

    data class ShowResults<T>(override val data: T) : UiState<T>() {
        override val showResults: Boolean = true
    }

    data object ShowHistory : UiState<Nothing>() {
        override val showHistory: Boolean = true
    }

    data object Loading : UiState<Nothing>() {
        override val showLoading = true
    }

    data class Error(override val errorMessage: String) : UiState<Nothing>()

    data object Empty : UiState<Nothing>() {
        override val showEmptyState = true
    }
}
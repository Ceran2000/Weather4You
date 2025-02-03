package pl.ceranka.weather4you.ui.search_city

sealed class UiState<out T> {

    open val data: T? = null
    open val showContent = false
    open val showInitial = false
    open val showLoading = false
    open val showError = false
    open val showEmptyState = false

    data class ShowContent<T>(override val data: T) : UiState<T>() {
        override val showContent: Boolean = true
    }

    data object Initial : UiState<Nothing>() {
        override val showInitial: Boolean = true
    }

    data object Loading : UiState<Nothing>() {
        override val showLoading = true
    }

    data class Error(val message: String) : UiState<Nothing>() {
        override val showError = true
    }

    data object Empty : UiState<Nothing>() {
        override val showEmptyState = true
    }
}
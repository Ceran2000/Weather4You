package pl.ceranka.weather4you.ui.base

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import pl.ceranka.weather4you.R
import pl.ceranka.weather4you.ui.util.UiText

abstract class BaseViewModel: ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        if (throwable is CancellationException) throw throwable
        showToast(R.string.unknown_error_message)
    }

    protected fun launchWithErrorHandling(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(exceptionHandler, block = block)
    }

    private val _toastMessage = Channel<UiText>()
    val toastMessage = _toastMessage.receiveAsFlow()

    protected fun showToast(message: String) {
        _toastMessage.trySend(UiText.DynamicString(message))
    }

    protected fun showToast(@StringRes resId: Int, vararg args: Any) {
        _toastMessage.trySend(UiText.StringResource(resId, *args))
    }

}
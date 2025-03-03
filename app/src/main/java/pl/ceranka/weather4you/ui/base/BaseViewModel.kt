package pl.ceranka.weather4you.ui.base

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import pl.ceranka.weather4you.ui.util.UiText

abstract class BaseViewModel: ViewModel() {

    private val _toastMessage = Channel<UiText>()
    val toastMessage = _toastMessage.receiveAsFlow()

    protected fun showToast(message: String) {
        _toastMessage.trySend(UiText.DynamicString(message))
    }

    protected fun showToast(@StringRes resId: Int, vararg args: Any) {
        _toastMessage.trySend(UiText.StringResource(resId, *args))
    }

}
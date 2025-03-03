package pl.ceranka.weather4you.ui.base

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun HandleToastMessages(viewModel: BaseViewModel) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.toastMessage.onEach {
            Toast.makeText(context, it.asString(context), Toast.LENGTH_SHORT).show()
        }.launchIn(this)
    }
}
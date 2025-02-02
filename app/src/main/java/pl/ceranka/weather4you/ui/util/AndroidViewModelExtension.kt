package pl.ceranka.weather4you.ui.util

import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import pl.ceranka.weather4you.Weather4YouApp

fun AndroidViewModel.showToast(message: String) {
    getApplication<Weather4YouApp>().run {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
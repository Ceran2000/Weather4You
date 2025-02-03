package pl.ceranka.weather4you.data.model

import androidx.compose.ui.graphics.Color

data class Temperature(val value: Int) {

    val uiValue: String by lazy { "$value*C" }

    val color: Color by lazy {
        when {
            value < 10 -> Color.Blue
            value < 20 -> Color.Black
            else -> Color.Red
        }
    }
}
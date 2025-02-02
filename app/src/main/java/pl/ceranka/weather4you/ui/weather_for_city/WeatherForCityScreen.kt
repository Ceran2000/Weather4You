package pl.ceranka.weather4you.ui.weather_for_city

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import pl.ceranka.weather4you.navigation.WeatherForCity

@Composable
fun WeatherForCityScreen(
    navController: NavController,
    viewModel: WeatherForCityViewModel = hiltViewModel()
) {
    val weather by viewModel.weather.collectAsStateWithLifecycle()

    if (weather == null) {
        Text("Ładuje")
    } else {
        Column {
            Text(weather!!.name)
            Text(weather!!.sys.country)
            Text(weather!!.weather[0].main)
            Text(weather!!.weather[0].description)
        }
    }

}
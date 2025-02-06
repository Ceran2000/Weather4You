package pl.ceranka.weather4you.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import pl.ceranka.weather4you.ui.search_city.SearchCityScreen
import pl.ceranka.weather4you.ui.weather_for_city.WeatherForCityScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier
) {
    NavHost(navController, startDestination = SearchCity, modifier = modifier) {
        composable<SearchCity>(
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }
        ) {
            SearchCityScreen(navController)
        }
        composable<WeatherForCity>(
            enterTransition = { slideInHorizontally(initialOffsetX = { -it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() }
        ) {
            WeatherForCityScreen(navController)
        }
    }
}

@Serializable
object SearchCity

@Serializable
data class WeatherForCity(val id: Int)
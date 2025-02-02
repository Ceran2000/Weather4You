package pl.ceranka.weather4you.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import pl.ceranka.weather4you.ui.search_city.SearchCityScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier
) {
    NavHost(navController, startDestination = SearchCity, modifier = modifier) {
        composable<SearchCity> {
            SearchCityScreen(navController)
        }
    }
}

@Serializable
object SearchCity
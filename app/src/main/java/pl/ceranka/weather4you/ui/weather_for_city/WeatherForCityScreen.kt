package pl.ceranka.weather4you.ui.weather_for_city

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import pl.ceranka.weather4you.data.model.forecast.Forecast
import pl.ceranka.weather4you.data.model.weather.Weather

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherForCityScreen(
    navController: NavController,
    viewModel: WeatherForCityViewModel = hiltViewModel()
) {
    val weatherState by viewModel.weatherUiState.collectAsStateWithLifecycle()
    val forecasts by viewModel.forecasts.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            //TODO: collapsing topbar
            CenterAlignedTopAppBar(
                title = { Text("Weather Details") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (weatherState.showLoading) {
            Loading(modifier = Modifier.fillMaxSize().padding(innerPadding))
        }

        if (weatherState.showError) {
            Error(
                errorMessage = weatherState.errorMessage.orEmpty(),
                onRefreshClicked = viewModel::onRefreshClicked,
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            )
        }

        if (weatherState.showContent) {
            Content(
                weather = weatherState.data!!,
                forecasts = forecasts ?: emptyList(),
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
            )
        }
    }

}

@Composable
private fun Content(
    weather: Weather,
    forecasts: List<Forecast>,
    modifier: Modifier
) {
    Column(
        modifier = modifier
    ) {
        MainInfo(
            weather = weather,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        AdditionalInfo(
            feelsLike = weather.tempFeelsLike.uiValue,
            visibilityInMeters = weather.visibilityInMeters,
            rainInPercent = forecasts.getOrNull(0)?.precipitationInPercentage,
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        HourlyForecast(forecasts)
    }
}

@Composable
private fun Loading(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun Error(
    errorMessage: String,
    onRefreshClicked: () -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            TextButton(
                onClick = onRefreshClicked,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Refresh")
            }
        }
    }
}
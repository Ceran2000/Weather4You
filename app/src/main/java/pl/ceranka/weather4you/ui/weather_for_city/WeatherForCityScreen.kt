package pl.ceranka.weather4you.ui.weather_for_city

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import pl.ceranka.weather4you.R
import pl.ceranka.weather4you.domain.model.forecast.Forecast
import pl.ceranka.weather4you.domain.model.weather.Weather
import pl.ceranka.weather4you.ui.base.HandleToastMessages
import pl.ceranka.weather4you.ui.components.CollapsingTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherForCityScreen(
    navController: NavController,
    viewModel: WeatherForCityViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val listState = rememberLazyListState()
    val isCollapsed: Boolean by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }

    val weatherState by viewModel.weatherUiState.collectAsStateWithLifecycle()
    val forecasts by viewModel.forecasts.collectAsStateWithLifecycle()

    HandleToastMessages(viewModel)

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CollapsingTopBar(
                title = weatherState.data?.cityName.orEmpty(),
                isCollapsed = isCollapsed,
                scrollBehavior = scrollBehavior,
                navigationAction = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        if (weatherState.showLoading) {
            Loading(modifier = Modifier.fillMaxSize().padding(innerPadding))
        }

        if (weatherState.showError) {
            Error(
                errorMessage = weatherState.errorMessage?.asString().orEmpty(),
                onRefreshClicked = viewModel::onRefreshClicked,
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            )
        }

        if (weatherState.showContent) {
            Content(
                weather = weatherState.data!!,
                forecasts = forecasts.orEmpty(),
                lazyListState = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
            )
        }
    }

}

@Composable
private fun Content(
    weather: Weather,
    forecasts: List<Forecast>,
    modifier: Modifier,
    lazyListState: LazyListState
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        item("city_name") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = weather.cityName,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        item("main_info") {
            MainInfo(
                weather = weather,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item("additional_info") {
            Spacer(modifier = Modifier.height(16.dp))

            AdditionalInfo(
                feelsLike = weather.tempFeelsLike.uiValue,
                visibilityInMeters = weather.visibilityInMeters,
                rainInPercent = forecasts.getOrNull(0)?.precipitationInPercentage,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        item("hourly_forecast") {
            Spacer(modifier = Modifier.height(16.dp))

            HourlyForecast(forecasts)
        }
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
                Text(text = stringResource(R.string.weather_for_city_refresh_button))
            }
        }
    }
}
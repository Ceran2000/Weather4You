package pl.ceranka.weather4you.ui.search_city

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import pl.ceranka.weather4you.R
import pl.ceranka.weather4you.domain.model.city.City
import pl.ceranka.weather4you.domain.model.city.Coord
import pl.ceranka.weather4you.navigation.WeatherForCity
import pl.ceranka.weather4you.ui.base.HandleToastMessages
import pl.ceranka.weather4you.ui.components.ProgressBarDialog
import pl.ceranka.weather4you.ui.components.TopBar
import pl.ceranka.weather4you.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchCityScreen(
    navController: NavController,
    viewModel: SearchCityViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val showSearchQueryError by viewModel.showSearchQueryError.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val initialState by viewModel.initialState.collectAsStateWithLifecycle()
    val recentCities by viewModel.recentCities.collectAsStateWithLifecycle()

    val onCityClicked: (City) -> Unit = {
        viewModel.onCityItemClicked(it)
        navController.navigate(WeatherForCity(it.id))
    }

    LaunchedEffect(Unit) {
        viewModel.navigateToWeatherForCity
            .onEach { navController.navigate(WeatherForCity(it)) }
            .launchIn(this)
    }

    HandleToastMessages(viewModel)

    HandlePermissionsRequest(viewModel)

    Scaffold(
        topBar = {
            TopBar(
                title = { Text(stringResource(R.string.app_name)) },
                navigationAction = null
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            SearchInputField(
                searchQuery = searchQuery,
                onSearchQueryChanged = viewModel::onSearchQueryChanged,
                onClearSearchQueryClicked = viewModel::onClearSearchQueryClicked,
                showError = showSearchQueryError,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .offset(x = -(12.dp))
                    .clip(CircleShape)
                    .clickable(onClick = viewModel::onGetLocationClicked)
                    .padding(vertical = 8.dp, horizontal = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationSearching,
                    contentDescription = null,
                )
                Text(
                    text = stringResource(R.string.search_city_use_location_button),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedContent(targetState = uiState, label = "ScreenStateAnim") { state ->
                when (state) {
                    is UiState.Initial -> {
                        InitialContent(
                            initialState = initialState,
                            modifier = Modifier.fillMaxWidth(),
                            cities = recentCities,
                            onItemClicked = onCityClicked,
                            onRemoveClicked = viewModel::onRemoveRecentCityClicked
                        )
                    }

                    is UiState.ShowResults -> SearchResultsList(
                        modifier = Modifier.fillMaxWidth(),
                        cities = uiState.data.orEmpty(),
                        onItemClicked = onCityClicked
                    )

                    is UiState.Loading -> Loading(modifier = Modifier.fillMaxWidth())

                    is UiState.Error -> Message(
                        text = uiState.errorMessage?.asString().orEmpty(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    is UiState.Empty -> Message(
                        text = stringResource(R.string.search_city_empty_state_message),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        if (viewModel.showLoading) {
            ProgressBarDialog()
        }
    }
}

@Composable
private fun InitialContent(
    initialState: InitialState,
    modifier: Modifier,
    cities: List<City>,
    onItemClicked: (City) -> Unit,
    onRemoveClicked: (City) -> Unit
) {
    when (initialState) {
        InitialState.HISTORY -> History(modifier, cities, onItemClicked, onRemoveClicked)
        InitialState.ANIMATION -> Animation(modifier)
    }
}

@Composable
private fun History(
    modifier: Modifier,
    cities: List<City>,
    onItemClicked: (City) -> Unit,
    onRemoveClicked: (City) -> Unit
) {
    Column(modifier) {
        Text(
            text = stringResource(R.string.search_city_recent_cities_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyColumn {
                items(cities, key = { it.id }) { city ->
                    RecentItem(city, onItemClicked, onRemoveClicked)
                }
            }
        }
    }
}

@Composable
private fun Animation(modifier: Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("anim_weather.json"))
    val progress by animateLottieCompositionAsState(composition = composition, iterations = LottieConstants.IterateForever)

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            modifier = Modifier.size(256.dp),
            composition = composition,
            progress = { progress }
        )
    }
}

@Composable
private fun SearchResultsList(
    modifier: Modifier,
    cities: List<City>,
    onItemClicked: (City) -> Unit
) {
    Column(modifier) {
        Text(
            text = stringResource(R.string.search_city_search_results_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            LazyColumn {
                items(cities, key = { it.id }) { city ->
                    SearchResultItem(
                        city = city,
                        onItemClicked = { onItemClicked(city) }
                    )
                }
            }
        }
    }
}

@Composable
private fun Loading(modifier: Modifier) {
    Box(
        modifier
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

// Preview

@Preview(showBackground = true)
@Composable
fun SearchResultsListPreview() {
    AppTheme {
        Surface(Modifier.padding(16.dp)) {
            SearchResultsList(
                modifier = Modifier.fillMaxWidth(),
                cities = listOf(
                    City(1, "London", "UK", Coord(51.5074, -0.1278)),
                    City(2, "Paris", "FR", Coord(48.8566, 2.3522)),
                    City(3, "New York", "US", Coord(40.7128, -74.0060))
                ),
                onItemClicked = {}
            )
        }
    }
}
package pl.ceranka.weather4you.ui.search_city

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import pl.ceranka.weather4you.R
import pl.ceranka.weather4you.data.model.city.City
import pl.ceranka.weather4you.navigation.WeatherForCity
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
    val recentCities by viewModel.recentCities.collectAsStateWithLifecycle()

    val onCityClicked: (City) -> Unit = {
        viewModel.onCityItemClicked(it)
        navController.navigate(WeatherForCity(it.id))
    }

    AppTheme {
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

                Spacer(modifier = Modifier.height(16.dp))

                AnimatedVisibility(visible = uiState.showResults) {
                    SearchResultsList(
                        modifier = Modifier.fillMaxWidth(),
                        cities = uiState.data ?: emptyList(),
                        onItemClicked = onCityClicked
                    )
                }

                AnimatedVisibility(visible = uiState.showHistory && recentCities.isNotEmpty()) {
                    RecentCitiesList(
                        modifier = Modifier.fillMaxWidth(),
                        cities = recentCities,
                        onItemClicked = onCityClicked,
                        onRemoveClicked = viewModel::onRemoveRecentCityClicked
                    )
                }

                if (uiState.showLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                if (uiState.showEmptyState) {
                    Message(
                        text = stringResource(R.string.search_city_empty_state_message),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (uiState.showError) {
                    Message(
                        text = uiState.errorMessage!!,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
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
private fun RecentCitiesList(
    modifier: Modifier,
    cities: List<City>,
    onItemClicked: (City) -> Unit,
    onRemoveClicked: (City) -> Unit
) {
    Column(
        modifier = modifier
    ) {
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
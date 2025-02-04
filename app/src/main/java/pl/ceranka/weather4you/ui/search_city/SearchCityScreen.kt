package pl.ceranka.weather4you.ui.search_city

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
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

    AppTheme {
        Scaffold(
            topBar = {
                TopBar(
                    title = { Text("Weather4You") },
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

                AnimatedVisibility(visible = uiState.showContent) {
                    SearchResultsList(
                        modifier = Modifier.fillMaxWidth(),
                        cities = uiState.data ?: emptyList(),
                        onCityClicked = {
                            viewModel.onCityItemClicked(it)     //TODO: test delay
                            navController.navigate(WeatherForCity(it.id))
                        }
                    )
                }

                AnimatedVisibility(visible = uiState.showInitial) {
                    RecentCitiesList(
                        modifier = Modifier
                            .weight(1f, fill = false),
                        cities = recentCities.map { it.title }      //TODO
                    )
                }

                if (uiState.showLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                if (uiState.showEmptyState) {
                    Text(
                        text = "No results",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (uiState.showError) {
                    Text(
                        text = uiState.errorMessage!!,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchInputField(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onClearSearchQueryClicked: () -> Unit,
    showError: Boolean,
    modifier: Modifier
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChanged,
        label = { Text("Wyszukaj miasto") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        trailingIcon = {
            IconButton(
                onClick = onClearSearchQueryClicked
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
            }
        },
        supportingText = {
            if (showError) {
                Text("Nazwa niepoprawna!", color = MaterialTheme.colorScheme.error)
            }
        },
        singleLine = true,
        shape = ShapeDefaults.Medium,
        modifier = modifier
    )
}

@Composable
private fun SearchResultsList(
    modifier: Modifier,
    cities: List<City>,
    onCityClicked: (City) -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        LazyColumn {
            items(cities, key = { it.id }) { city ->
                SearchResultItem(
                    city = city,
                    onItemClicked = { onCityClicked(city) }
                )
            }
        }
    }
}

@Composable
private fun SearchResultItem(
    city: City,
    onItemClicked: () -> Unit
) {
    ListItem(
        title = city.title,
        subTitle = city.subTitle,
        leadingIcon = Icons.Default.LocationOn,
        trailingIcon = Icons.Default.ChevronRight,
        onItemClicked = onItemClicked
    )
}

@Composable
private fun RecentCitiesList(
    modifier: Modifier,
    cities: List<String>,
    onRemoveClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Recent searches",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyColumn {
                itemsIndexed(cities, key = { index, _ -> index }) { index, item ->
                    RecentItem(item)
                }
            }
        }
    }
}

@Composable
private fun RecentItem(text: String) {
    ListItem(
        title = text,
        leadingIcon = Icons.Default.AccessTime,
        trailingIcon = Icons.Default.Close
    )
}

@Composable
private fun ListItem(
    title: String,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onItemClicked: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(enabled = onItemClicked != null) { onItemClicked!!.invoke() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Text(
            text = title,
            modifier = Modifier
                .weight(1f, true)
                .padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        if (trailingIcon != null) {
            Icon(
                imageVector = trailingIcon,
                contentDescription = null,
                modifier = Modifier.padding(end = 16.dp)
            )
        }

    }
}

@Composable
private fun ListItem(
    title: String,
    subTitle: String,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onItemClicked: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clickable(enabled = onItemClicked != null) { onItemClicked!!.invoke() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f, true)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subTitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }



        if (trailingIcon != null) {
            Icon(
                imageVector = trailingIcon,
                contentDescription = null,
                modifier = Modifier.padding(end = 16.dp)
            )
        }

    }
}
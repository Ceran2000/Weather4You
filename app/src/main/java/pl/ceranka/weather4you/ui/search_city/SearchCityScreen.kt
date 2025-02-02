package pl.ceranka.weather4you.ui.search_city

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import pl.ceranka.weather4you.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchCityScreen(
    navController: NavController,
    viewModel: SearchCityViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val cities by viewModel.cities.collectAsStateWithLifecycle()
    val showResults = cities.isNotEmpty()
    val showRecentCities = recentCities.isNotEmpty()

    AppTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(title = { Text("Weather4You") })
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = viewModel::onSearchQueryChanged,
                    label = { Text("Wyszukaj miasto") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    },
                    shape = ShapeDefaults.Medium,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )

                if (showResults) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        cities.forEach {
                            SearchResultItem(text = "${it.name}, ${it.sys.country}")
                        }
                    }
                }

                if (showRecentCities) {
                    Text(
                        text = "Recent searches",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        recentCities.forEach {
                            RecentItem(it)
                        }
                    }
                }


            }
        }
    }
}

private val recentCities: List<String> = listOf(
    "New York, United States",
    "Łódź, Poland",
    "Warszawa, Poland"
)

@Composable
private fun SearchResultItem(text: String) {
    ListItem(
        title = text,
        leadingIcon = Icons.Default.LocationOn,
        trailingIcon = Icons.Default.ChevronRight
    )
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
    trailingIcon: ImageVector? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
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
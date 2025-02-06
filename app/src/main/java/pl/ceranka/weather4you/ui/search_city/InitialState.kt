package pl.ceranka.weather4you.ui.search_city

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import pl.ceranka.weather4you.R
import pl.ceranka.weather4you.domain.model.city.City

enum class InitialState {

    ANIMATION {
        @Composable
        override fun Content(
            modifier: Modifier,
            cities: List<City>,
            onItemClicked: (City) -> Unit,
            onRemoveClicked: (City) -> Unit
        ) {
            Animation(modifier)
        }
    },

    HISTORY {
        @Composable
        override fun Content(
            modifier: Modifier,
            cities: List<City>,
            onItemClicked: (City) -> Unit,
            onRemoveClicked: (City) -> Unit
        ) {
            History(modifier, cities, onItemClicked, onRemoveClicked)
        }
    };

    @Composable
    abstract fun Content(
        modifier: Modifier,
        cities: List<City>,
        onItemClicked: (City) -> Unit,
        onRemoveClicked: (City) -> Unit
    )
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
private fun Animation(
    modifier: Modifier
) {
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
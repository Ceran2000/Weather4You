package pl.ceranka.weather4you.ui.weather_for_city

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import pl.ceranka.weather4you.R
import pl.ceranka.weather4you.domain.model.Temperature
import pl.ceranka.weather4you.domain.model.forecast.Forecast
import pl.ceranka.weather4you.domain.model.weather.Weather

@Composable
fun MainInfo(
    weather: Weather,
    modifier: Modifier
) {
    Column(
        modifier = modifier
    ) {
        AsyncImage(
            model = weather.icon.url,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(128.dp)
                .padding(top = 16.dp)
        )
        Text(
            text = weather.temp.uiValue,
            fontSize = 24.sp,
            color = weather.temp.color,
            modifier = Modifier
                .padding(top = 16.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = weather.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            InfoBox(
                iconResId = R.drawable.ic_humidity,
                name = stringResource(R.string.weather_for_city_humidity_label),
                value = "${weather.humidity}%",
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            InfoBox(
                iconResId = R.drawable.ic_cloudiness,
                name = stringResource(R.string.weather_for_city_cloudiness_label),
                value = "${weather.cloudinessInPercentage}%",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun AdditionalInfo(
    feelsLike: String,
    visibilityInMeters: Int,
    rainInPercent: Int?,
    modifier: Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.weather_for_city_additional_information_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            AdditionalInfoRow(
                iconResId = R.drawable.ic_temp_feels_like,
                title = stringResource(R.string.weather_for_city_feels_like_label),
                value = feelsLike
            )
            Spacer(modifier = Modifier.height(8.dp))
            AdditionalInfoRow(
                iconResId = R.drawable.ic_visibility,
                title = stringResource(R.string.weather_for_city_visibility_label),
                value = "${visibilityInMeters}m"
            )
            if (rainInPercent != null) {
                Spacer(modifier = Modifier.height(8.dp))
                AdditionalInfoRow(
                    iconResId = R.drawable.ic_precipitation,
                    title = stringResource(R.string.weather_for_city_precipitation_label),
                    value = "${rainInPercent}%"
                )
            }
        }
    }
}

@Composable
fun HourlyForecast(forecasts: List<Forecast>) {
    AnimatedVisibility(
        visible = forecasts.isNotEmpty()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Text(
                text = stringResource(R.string.weather_for_city_hourly_forecast_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(16.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                items(items = forecasts) {forecast ->
                    HourlyForecastItem(
                        forecast = forecast,
                        iconUrl = forecast.icon.url,
                        temp = forecast.temp
                    )
                }
            }
        }
    }
}

@Composable
private fun AdditionalInfoRow(
    @DrawableRes iconResId: Int,
    title: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(iconResId),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun InfoBox(
    @DrawableRes iconResId: Int,
    name: String,
    value: String,
    modifier: Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(iconResId),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    maxLines = 2,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun HourlyForecastItem(
    forecast: Forecast,
    iconUrl: String,
    temp: Temperature
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = forecast.timeUiValue,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Text(
                text = forecast.dayOfWeekUiValue,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            AsyncImage(
                model = iconUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
            )
            Text(
                text = temp.uiValue,
                style = MaterialTheme.typography.labelMedium,
                color = temp.color
            )
        }
    }
}
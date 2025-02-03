package pl.ceranka.weather4you.ui.weather_for_city

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Adb
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import pl.ceranka.weather4you.data.model.Temperature
import pl.ceranka.weather4you.data.model.forecast.Forecast
import pl.ceranka.weather4you.data.model.weather.Weather
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

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
            color = MaterialTheme.colorScheme.onSurface,
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
            //Humidity
            InfoBox(
                icon = Icons.Default.LocationOn,
                name = "Humidity",
                value = "${weather.humidity} %",
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            //Cloudiness (clouds)
            InfoBox(
                icon = Icons.Default.AddLocation,
                name = "Cloudiness",
                value = "${weather.cloudinessInPercentage} %",
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
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Additional Information",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            AdditionalInfoRow(
                icon = Icons.Default.AccessTime,
                title = "Feels like",
                value = feelsLike
            )
            Spacer(modifier = Modifier.height(8.dp))
            AdditionalInfoRow(
                icon = Icons.Default.AccountBalance,
                title = "Visibility",
                value = "$visibilityInMeters m"
            )
            if (rainInPercent != null) {
                Spacer(modifier = Modifier.height(8.dp))
                AdditionalInfoRow(
                    icon = Icons.Default.Adb,
                    title = "Precipitation",
                    value = "$rainInPercent %"
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
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Hourly Forecast",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(16.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                items(items = forecasts) {forecast ->
                    HourlyForecastItem(
                        dateTimeSecondsUTC = forecast.dateTimeSecondsUTC,
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
    icon: ImageVector,
    title: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun InfoBox(
    icon: ImageVector,
    name: String,
    value: String,
    modifier: Modifier
) {
    Card(
        modifier
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = icon, contentDescription = null)
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun HourlyForecastItem(
    dateTimeSecondsUTC: Int,
    iconUrl: String,
    temp: Temperature
) {
    val zonedDateTime = remember { Instant.ofEpochSecond(dateTimeSecondsUTC.toLong()).atZone(ZoneId.systemDefault()) }
    val day = remember { zonedDateTime.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()) }
    val time = remember { zonedDateTime.format(DateTimeFormatter.ofPattern("HH:mm")) }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = time,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = day,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
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
package pl.ceranka.weather4you.ui.search_city

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pl.ceranka.weather4you.R
import pl.ceranka.weather4you.data.model.city.City

@Composable
fun SearchInputField(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onClearSearchQueryClicked: () -> Unit,
    showError: Boolean,
    modifier: Modifier
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChanged,
        label = { Text(stringResource(R.string.search_city_search_input_field_label)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        },
        trailingIcon = {
            IconButton(
                onClick = onClearSearchQueryClicked
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        supportingText = {
            if (showError) {
                Text(
                    text = stringResource(R.string.search_city_search_input_field_error_message),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        singleLine = true,
        shape = ShapeDefaults.Medium,
        modifier = modifier
    )
}

@Composable
fun SearchResultItem(
    city: City,
    onItemClicked: () -> Unit
) {
    ListItem(
        title = city.title,
        subTitle = city.subTitle,
        leadingIcon = Icons.Default.LocationOn,
        trailingIcon = Icons.Default.ChevronRight,
        onItemClicked = onItemClicked,
        onTrailingIconClicked = onItemClicked
    )
}

@Composable
fun RecentItem(
    city: City,
    onItemClicked: (City) -> Unit,
    onRemoveClicked: (City) -> Unit
) {
    ListItem(
        title = city.title,
        subTitle = city.subTitle,
        leadingIcon = Icons.Default.AccessTime,
        trailingIcon = Icons.Default.Close,
        onItemClicked = { onItemClicked(city) },
        onTrailingIconClicked = { onRemoveClicked(city) }
    )
}

@Composable
private fun ListItem(
    title: String,
    subTitle: String,
    leadingIcon: ImageVector,
    trailingIcon: ImageVector? = null,
    onItemClicked: () -> Unit,
    onTrailingIconClicked: (() -> Unit)? = null
) {
    val showTrailingIcon = trailingIcon != null

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clickable(onClick = onItemClicked),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = null,
            modifier = Modifier.padding(start = 16.dp).size(24.dp)
        )

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

        if (showTrailingIcon) {
            IconButton(
                onClick = { onTrailingIconClicked?.invoke() },
                colors = IconButtonDefaults.iconButtonColors(disabledContentColor = LocalContentColor.current),
                modifier = Modifier.padding(end = 4.dp)
            ) {
                Icon(
                    imageVector = trailingIcon!!,
                    contentDescription = null
                )
            }
        }

    }
}

@Composable
fun Message(
    text: String,
    modifier: Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}
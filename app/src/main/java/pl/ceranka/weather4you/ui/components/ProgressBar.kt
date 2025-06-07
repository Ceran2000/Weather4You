package pl.ceranka.weather4you.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun ProgressBarDialog() {
    Dialog(onDismissRequest = {  }, properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)) {
        ElevatedCard(elevation = CardDefaults.elevatedCardElevation(4.dp)) {
            CircularProgressIndicator(modifier = Modifier.padding(24.dp))
        }
    }
}
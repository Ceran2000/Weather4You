package pl.ceranka.weather4you.ui.search_city

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import pl.ceranka.weather4you.R
import pl.ceranka.weather4you.util.showToast

@Composable
fun HandlePermissionsRequest(viewModel: SearchCityViewModel) {
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (!fineLocationGranted && !coarseLocationGranted) {
            //TODO: show dialog
            context.showToast(R.string.search_city_location_permission_denied_message)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.requestLocationPermissions
            .onEach { permissions ->
                permissionLauncher.launch(permissions)
            }
            .launchIn(this)
    }
}
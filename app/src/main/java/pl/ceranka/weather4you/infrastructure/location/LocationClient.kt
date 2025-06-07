package pl.ceranka.weather4you.infrastructure.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import pl.ceranka.weather4you.infrastructure.util.isPermissionGranted
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class LocationClient @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) {
    companion object {
        private val permissions = arrayOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private val isAnyLocationPermissionGranted: Boolean get() = permissions.any { context.isPermissionGranted(it) }

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Location? {
        if (!isAnyLocationPermissionGranted) throw LocationPermissionsRequiredException(permissions)
        return fusedLocationProviderClient.lastLocation.await()?.toLocal()
    }
}

private suspend fun<T> Task<T>.await(): T? = suspendCancellableCoroutine { continuation ->
    addOnSuccessListener { result ->
        continuation.resume(result)
    }
    addOnFailureListener { exception ->
        continuation.resumeWithException(exception)
    }
    addOnCanceledListener {
        continuation.cancel()
    }
}
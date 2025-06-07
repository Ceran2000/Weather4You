package pl.ceranka.weather4you.infrastructure.util

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.isPermissionGranted(permission: String): Boolean =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
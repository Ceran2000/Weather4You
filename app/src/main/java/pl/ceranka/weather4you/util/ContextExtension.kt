package pl.ceranka.weather4you.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.showToast(text: String) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

fun Context.showToast(@StringRes textResId: Int) = Toast.makeText(this, getString(textResId), Toast.LENGTH_SHORT).show()
package pl.ceranka.weather4you.ui.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UiText {

    data class DynamicString(val value: String): UiText() {

        @Composable
        override fun asString(): String = value

        override fun asString(context: Context): String = value
    }

    class StringResource(@StringRes private val resId: Int, private vararg val args: Any): UiText() {

        @Composable
        override fun asString(): String = stringResource(resId, *args)

        override fun asString(context: Context): String = context.getString(resId, *args)

    }


    @Composable abstract fun asString(): String

    abstract fun asString(context: Context): String
}
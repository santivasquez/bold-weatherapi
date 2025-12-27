package com.example.bold_weather_api.ui.common

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface UiText {
    data class Dynamic(val value: String) : UiText
    data class StringRes(
        @androidx.annotation.StringRes val resId: Int,
        val args: List<Any> = emptyList(),
    ) : UiText
}

@Composable
fun UiText.asString(): String =
    when (this) {
        is UiText.Dynamic -> value
        is UiText.StringRes -> stringResource(resId, *args.toTypedArray())
    }

fun UiText.asString(context: Context): String =
    when (this) {
        is UiText.Dynamic -> value
        is UiText.StringRes -> context.getString(resId, *args.toTypedArray())
    }



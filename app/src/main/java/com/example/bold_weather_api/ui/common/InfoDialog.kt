package com.example.bold_weather_api.ui.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.bold_weather_api.R

@Composable
fun InfoDialog(
    title: UiText,
    message: UiText,
    onDismiss: () -> Unit,
    onRetry: (() -> Unit)? = null,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title.asString()) },
        text = { Text(message.asString()) },
        confirmButton = {
            if (onRetry != null) {
                TextButton(onClick = onRetry) {
                    Text(stringResource(R.string.common_retry))
                }
            } else {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.common_ok))
                }
            }
        },
        dismissButton = {
            if (onRetry != null) {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.common_close))
                }
            }
        },
    )
}




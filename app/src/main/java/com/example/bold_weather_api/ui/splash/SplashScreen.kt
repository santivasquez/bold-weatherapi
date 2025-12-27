package com.example.bold_weather_api.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bold_weather_api.R
import com.example.bold_weather_api.ui.theme.BoldweatherapiTheme

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
) {

    val background = Brush.horizontalGradient(
        listOf(
            Color(0xFF1B2A7A),
            Color(0xFFF23A57),
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(background),
        contentAlignment = Alignment.Center,
    ) {

        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.app_logo),
                contentDescription = stringResource(R.string.splash_logo_cd),
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(width = 2.dp, color = Color.White, shape = CircleShape),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.splash_title),
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.splash_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = stringResource(R.string.splash_by),
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(R.string.splash_author),
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun SplashScreenPreview() {
    BoldweatherapiTheme {
        SplashScreen()
    }
}

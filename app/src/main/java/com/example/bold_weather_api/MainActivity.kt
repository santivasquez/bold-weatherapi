package com.example.bold_weather_api

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.bold_weather_api.navigation.AppNavHost
import com.example.bold_weather_api.ui.theme.BoldweatherapiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BoldweatherapiTheme {
                AppNavHost()
            }
        }
    }
}
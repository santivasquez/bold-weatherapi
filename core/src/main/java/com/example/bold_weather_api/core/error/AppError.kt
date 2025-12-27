package com.example.bold_weather_api.core.error

sealed interface AppError {
    data object Network : AppError
    data object Timeout : AppError
    data class Http(val code: Int, val message: String? = null) : AppError
    data object Serialization : AppError
    data class Unknown(val message: String? = null) : AppError
}



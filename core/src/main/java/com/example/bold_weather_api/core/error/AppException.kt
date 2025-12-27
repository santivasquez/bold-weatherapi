package com.example.bold_weather_api.core.error

class AppException(
    val error: AppError,
    cause: Throwable? = null,
) : RuntimeException(cause)



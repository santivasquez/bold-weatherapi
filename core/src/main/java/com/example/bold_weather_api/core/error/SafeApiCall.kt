package com.example.bold_weather_api.core.error

import com.squareup.moshi.JsonDataException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

inline fun <T> safeApiCall(block: () -> T): T =
    try {
        block()
    } catch (t: Throwable) {
        throw AppException(mapToAppError(t), t)
    }

fun mapToAppError(t: Throwable): AppError =
    when (t) {
        is AppException -> t.error
        is SocketTimeoutException -> AppError.Timeout
        is IOException -> AppError.Network
        is JsonDataException -> AppError.Serialization
        is HttpException -> AppError.Http(code = t.code(), message = t.message())
        else -> AppError.Unknown(message = t.message)
    }



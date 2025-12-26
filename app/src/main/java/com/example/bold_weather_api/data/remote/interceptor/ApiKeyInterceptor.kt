package com.example.bold_weather_api.data.remote.interceptor

import com.example.bold_weather_api.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalUrl = original.url

        val apiKey = BuildConfig.WEATHER_API_KEY
        val urlWithKey = if (apiKey.isNotBlank() && originalUrl.queryParameter("key") == null) {
            originalUrl.newBuilder()
                .addQueryParameter("key", apiKey)
                .build()
        } else {
            originalUrl
        }

        val request = original.newBuilder()
            .url(urlWithKey)
            .build()

        return chain.proceed(request)
    }
}

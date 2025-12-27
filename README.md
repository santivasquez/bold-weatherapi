# Bold WeatherAPI (Android)

Android app (Kotlin + Jetpack Compose) that lets you search locations and view current weather + 3-day forecast using [WeatherAPI docs](https://www.weatherapi.com/docs/).

## Features (baseline requirements)
- Splash screen
- Search locations **as you type**
- Results show **name** + **country**
- Detail screen shows:
  - **Current**: temp (°C) + condition text + icon
  - **3 days**: average temp (°C) + condition text + icon
- Supports orientation (portrait/landscape) with responsive 2‑pane layouts on wide screens
- “Use my location” (requests runtime permissions and navigates to forecast)

## Tech stack
- **UI**: Jetpack Compose (Material 3)
- **Architecture**: Multi-module Clean Architecture + MVVM
- **DI**: Hilt
- **Networking**: Retrofit + OkHttp + Moshi
- **Images**: Coil
- **Async**: Coroutines + Flow
- **Navigation**: Navigation Compose

## Setup

### 1) Requirements
- Android Studio (latest stable)
- SDK 21

### 2) API key (no hardcode)
Add your WeatherAPI key to `local.properties` (root):

```properties
WEATHER_API_KEY=de5553176da64306b86153651221606
```

The app reads it at build time and injects it into `data.BuildConfig.WEATHER_API_KEY` (the `:data` module).

### 3) Build & run
From repo root:

```bash
./gradlew :app:assembleDebug
```

Run on device/emulator from Android Studio or install the generated APK.

## Tests

Run unit tests:

```bash
./gradlew :data:testDebugUnitTest
./gradlew :presentation:testDebugUnitTest
```

## Project structure (current)
This project uses a multi-module setup:

- `:app` (Android application): entrypoint only (`MainActivity`, `Application`, manifest).
- `:presentation` (Android library): Compose UI, navigation, ViewModels, UI helpers, and its own resources (`presentation.R`).
- `:data` (Android library): Retrofit/OkHttp/Moshi, DTOs + mappers, repository implementations, Hilt modules, and `data.BuildConfig.WEATHER_API_KEY`.
- `:domain` (pure Kotlin): models, repository interfaces, and use cases.
- `:core` (Android library): error types and `safeApiCall` error mapping.

## Notes
- Location search uses debounce to avoid excessive API calls.
- Forecast uses `q=lat,lon` to avoid ambiguity in city names.


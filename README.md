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
- **Architecture**: Clean-ish layering inside `:app` (domain/data/presentation packages) + MVVM
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

The app reads it at build time and injects it into `BuildConfig.WEATHER_API_KEY`.

### 3) Build & run
From repo root:

```bash
./gradlew :app:assembleDebug
```

Run on device/emulator from Android Studio or install the generated APK.

## Tests

Run unit tests:

```bash
./gradlew :app:testDebugUnitTest
```

## Project structure (current)
We’re still in a single Gradle module (`:app`) to keep iteration fast. Inside it we keep package boundaries:
- `domain/`: business models + repository interfaces + use cases
- `data/`: Retrofit DTOs, mappers, repository implementations
- `ui/`: Compose screens + ViewModels (MVVM)
- `di/`: Hilt modules

## Notes
- Location search uses debounce + cancellation (`flatMapLatest`) to avoid multiple in-flight calls.
- Forecast uses `q=lat,lon` to avoid ambiguity in city names.


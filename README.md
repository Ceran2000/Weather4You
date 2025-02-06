# Weather4You

Weather4You is an Android mobile application that allows you to check the weather forecasts for selected cities. The app enables searching for cities, displaying current weather and forecasts for the next hours.

## Requirements

To run the app on your device, you need to provide an API key from OpenWeatherMap to get the weather data.

### Step 1: Get an API Key

1. Sign up on [OpenWeatherMap](https://openweathermap.org/api).
2. Get your API key.

### Step 2: Add the API Key to the `local.properties` file

Go to the root folder of the project and open (or create if it doesn't exist) the `local.properties` file.
Add the following line, replacing `your_api_key` with your actual API key:
```OPEN_WEATHER_API_KEY=your_api_key```

## Features

- **City Search**: Search for cities by name.
- **Weather Display**: Current weather and forecasts for the next hours.
- **Search History Management**: The history of searched cities is saved locally.
- **Multilingual Support**: The app adjusts the language based on the device's settings.

## Technologies
- **Kotlin** - Programming language.
- **MVVM** - Architectural pattern.
- **Jetpack Compose** - UI.
- **Navigation**
- **Hilt** - Dependency Injection.
- **Retrofit** - Communication with APIs.
- **Room** - Local data storage (city search history).
- **Material Design 3** - UI design guidelines by Google.
- **Flow**
- **Coroutines**

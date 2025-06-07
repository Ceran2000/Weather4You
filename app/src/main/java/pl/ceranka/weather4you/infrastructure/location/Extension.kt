package pl.ceranka.weather4you.infrastructure.location

fun android.location.Location.toLocal() = Location(latitude, longitude)
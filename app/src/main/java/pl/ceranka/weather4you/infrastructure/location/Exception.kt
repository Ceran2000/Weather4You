package pl.ceranka.weather4you.infrastructure.location

class LocationPermissionsRequiredException(val permissions: Array<String>) : Exception("Missing location permission")
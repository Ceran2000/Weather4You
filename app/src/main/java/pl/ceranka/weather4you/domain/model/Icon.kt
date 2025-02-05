package pl.ceranka.weather4you.domain.model

data class Icon(private val iconCode: String) {
    val url: String by lazy { "https://openweathermap.org/img/wn/$iconCode@2x.png" }
}
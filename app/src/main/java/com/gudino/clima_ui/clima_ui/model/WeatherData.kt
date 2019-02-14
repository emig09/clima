package com.gudino.clima_ui.clima_ui.model

data class WeatherData(val main: Main, val weather: List<Weather>, val clouds: Cloud, val wind: Wind, val dt_txt: String)
package com.example.weatherapp.api

import retrofit2.http.GET
import retrofit2.http.Query

data class WeatherResponse(
    val cod: String,
    val message: Int,
    val cnt: String,
    val list: List<WeatherData>,
    val city: CityData,
)

data class CityData(
    val name: String,
)

data class WeatherData(
    val dt: Long,
    val main: Main,
    val wind: Winds,
    val weather: List<Weather>,
    val dt_txt: String,
)

data class Weather(
    val icon: String,
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Double,
    val sea_level: Double,
    val grnd_level: Double,
    val humidity: Double,
    val temp_kf: Double
)

data class Winds(
    val speed: Double,
    val deg: Double,
    val gust: Double
)

interface WeatherApiService {
    @GET("data/2.5/forecast")
    suspend fun getWeatherForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String
    ): WeatherResponse
}

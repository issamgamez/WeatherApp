package com.example.weatherapp

import retrofit2.Call
import retrofit2.http.GET


interface WeatherService {
    @GET("?q=Casablanca&appid=e219c008845485f330e0f909b44212f3&units=metric")
    fun GetWeatherByCity(): Call<String>
}
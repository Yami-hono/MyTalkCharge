package com.example.mytalkcharge.network

import com.example.mytalkcharge.TownWeather
import com.example.mytalkcharge.repository.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {

    @GET("forecast/")
    suspend fun getWeatherInfo(@Query("id")id:Int, @Query("appid")appid:String): Response<WeatherResponse>

    @GET("weather/")
    suspend fun getWeatherInfo(@Query("q")state:String, @Query("appid")appid:String): Response<TownWeather>
}
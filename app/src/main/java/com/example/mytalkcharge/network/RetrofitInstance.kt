package com.example.mytalkcharge.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object{
        val baseUrl="https://api.openweathermap.org/data/2.5/"


//        api.openweathermap.org/data/2.5/forecast/daily

        fun getRetrofitInstance():Retrofit {

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

    }

}
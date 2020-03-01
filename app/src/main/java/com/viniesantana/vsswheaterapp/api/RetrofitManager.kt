package com.viniesantana.vsswheaterapp.api

import com.viniesantana.vsswheaterapp.Const
import com.viniesantana.vsswheaterapp.WeatherService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitManager{

    val instance = Retrofit.Builder()
        .baseUrl(Const.WEATHER_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getWeatherService() = instance.create(WeatherService::class.java)

}
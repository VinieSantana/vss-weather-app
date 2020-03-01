package com.viniesantana.vsswheaterapp

import com.viniesantana.vsswheaterapp.entity.FindResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService{

    @GET("find")
    fun find(
        @Query("q") cityName: String,
        @Query("units") tempoUnit: String,
        @Query("lang") language: String,
        @Query("appid") appId : String ): Call<FindResult>

    @GET("group")
    fun group(
        @Query("id") cityId: String,
        @Query("units") tempoUnit: String,
        @Query("lang") language: String,
        @Query("appid") appId : String ): Call<FindResult>

}
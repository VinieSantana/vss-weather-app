package com.viniesantana.vsswheaterapp.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class FindResult(
    @SerializedName("list")
    val items: List<City>? = null)

data class City(
    val id : Int = 0,
    val name : String = "",
    @SerializedName("weather")
    val weatherList : List<Weather>,
    val main : Main,
    val wind : Wind,
    val clouds: Clouds
)

data class Weather(
    val description : String = "",
    val icon : String = ""
)

data class Main(
    val pressure : Float = 0f,
    val temp : Float = 0f
)

data class Wind(
    val speed : Float = 0f
)

data class Clouds(
    val all : Int = 0
)

@Entity(tableName = "tb_city")
data class FavoriteCity(
    @PrimaryKey
    val id : Int = 0,
    val name : String = ""
)
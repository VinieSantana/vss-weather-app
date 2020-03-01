package com.viniesantana.vsswheaterapp.database

import androidx.room.*
import com.viniesantana.vsswheaterapp.entity.FavoriteCity


@Dao
interface FavoriteCityDao {

    @Query("SELECT * FROM TB_CITY")
    fun getFavoriteCities() : List<FavoriteCity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCity(favoriteCity: FavoriteCity)

    @Delete
    fun deleteCity(favoriteCity: FavoriteCity)

}
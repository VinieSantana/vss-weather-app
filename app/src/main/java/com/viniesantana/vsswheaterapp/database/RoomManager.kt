package com.viniesantana.vsswheaterapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.viniesantana.vsswheaterapp.entity.FavoriteCity

@Database(entities = [FavoriteCity::class], version = 1)
abstract class RoomManager : RoomDatabase() {

    abstract fun getFavoriteDao() : FavoriteCityDao

    companion object {
        private var INSTANCE : RoomManager? = null

        fun instance(context: Context) : RoomManager {
            synchronized(RoomManager::class.java) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        RoomManager::class.java,
                        "weather.db")
//                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE!!
        }
    }

}
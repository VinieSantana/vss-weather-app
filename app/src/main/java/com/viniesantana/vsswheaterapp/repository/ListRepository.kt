package com.viniesantana.vsswheaterapp.repository

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.viniesantana.vsswheaterapp.Const
import com.viniesantana.vsswheaterapp.api.RetrofitManager
import com.viniesantana.vsswheaterapp.database.RoomManager
import com.viniesantana.vsswheaterapp.entity.City
import com.viniesantana.vsswheaterapp.entity.FavoriteCity
import com.viniesantana.vsswheaterapp.entity.FindResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListRepository{

    private val currentList = MutableLiveData<List<City>>()
    private val favoritesCities = MutableLiveData<List<FavoriteCity>>()


    fun updatedList(): LiveData<List<City>> = this.currentList
    fun updatedFavorites(): LiveData<List<FavoriteCity>> = this.favoritesCities

    fun getFavoritesCities(context : Context): LiveData<List<FavoriteCity>>{
        val items = MutableLiveData<List<FavoriteCity>>()
        val task = object : AsyncTask<Void, Void, List<FavoriteCity>>() {

            override fun onPreExecute() {
            }

            override fun doInBackground(vararg p0: Void?): List<FavoriteCity> {
                return RoomManager.instance(context)
                    .getFavoriteDao()
                    .getFavoriteCities()
            }

            override fun onPostExecute(result: List<FavoriteCity>?) {
                super.onPostExecute(result)
                var citiesIds = ""
                items.value = result
                result?.let {
                    this@ListRepository.favoritesCities.value = it
                    citiesIds = it.joinToString(",", "", "", -1, "") {
                        it.id.toString()
                    }
                    group(context, citiesIds)
                }
            }
        }
        task.execute()
        return items
    }

    fun find(context: Context, city: String){

        val call = RetrofitManager.getWeatherService()
            .find(
                city,
                context.getSharedPreferences(Const.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Const.TEMPERATURE_UNIT, Const.CELSIUS) ?: Const.CELSIUS,
                context.getSharedPreferences(Const.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Const.LANGUAGE, Const.ENGLISH) ?: Const.ENGLISH,
                Const.WEATHER_APP_KEY
            )

        updateCurrentList(call)

    }

    fun group(context: Context, ids: String){

        val call = RetrofitManager.getWeatherService()
            .group(
                ids,
                context.getSharedPreferences(Const.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Const.TEMPERATURE_UNIT, Const.CELSIUS) ?: Const.CELSIUS,
                context.getSharedPreferences(Const.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Const.LANGUAGE, Const.ENGLISH) ?: Const.ENGLISH,
                Const.WEATHER_APP_KEY
            )

        updateCurrentList(call)
    }

    private fun updateCurrentList(call: Call<FindResult>){
        call.enqueue(object : Callback<FindResult> {

            override fun onFailure(call: Call<FindResult>, t: Throwable) {
            }

            override fun onResponse(call: Call<FindResult>, response: Response<FindResult>) {
//                setViewUnits()
                if (response.isSuccessful) {
                    response.body()?.let {
                        it.items?.let { list ->
                            this@ListRepository.currentList.value = list
                        }
                    }
                }else{
                    this@ListRepository.currentList.value = null
                }
            }
        })
    }


     fun changeFavorites(context: Context, city: City, delete: Boolean) {
         val roomManager = RoomManager.instance(context)
        val task = object : AsyncTask<Void, Void, List<FavoriteCity>>() {
            override fun doInBackground(vararg p0: Void?): List<FavoriteCity> {
                if (delete) {
                    roomManager.getFavoriteDao()
                        .deleteCity(FavoriteCity(city.id, city.name))
                } else {
                    roomManager.getFavoriteDao()
                        .addCity(FavoriteCity(city.id, city.name))
                }
                return roomManager
                    .getFavoriteDao()
                    .getFavoriteCities()
            }

            override fun onPostExecute(result: List<FavoriteCity>?) {
                super.onPostExecute(result)
                this@ListRepository.favoritesCities.value = result
            }
        }
        task.execute()
    }
}


package com.viniesantana.vsswheaterapp.feature.list

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.viniesantana.vsswheaterapp.entity.City
import com.viniesantana.vsswheaterapp.entity.FavoriteCity
import com.viniesantana.vsswheaterapp.repository.ListRepository

class ListViewModel : ViewModel() {

    private val citiesList: LiveData<List<City>>?
    private val favorites: LiveData<List<FavoriteCity>>
    private val listRepository = ListRepository()
    private var initial = true
    private var lastSearch = ""
    private var language : String? = null
    private var tempUnit : String? = null

    init {
        favorites = listRepository.updatedFavorites()
        citiesList = listRepository.updatedList()
    }

    fun observerFavorites() = favorites
    fun observerList() = citiesList
    fun findCity(context: Context, city: String) {
        if (city == "") {
            listRepository.getFavoritesCities(context)
        } else {
            listRepository.find(context, city)
        }
        lastSearch = city
    }

    fun setInitialPreferences(language: String, tempUnit: String){
        this.language = language
        this.tempUnit = tempUnit
    }

    fun getLanguage() = this.language

    fun getTempUnit() = this.tempUnit

    fun updateSearch(context: Context){
        findCity(context, lastSearch)
    }

    fun changeFavorites(context: Context, city: City, delete: Boolean){
        listRepository.changeFavorites(context, city, delete)
    }

    fun initFavorites(context: Context) {
        if (initial) {
            listRepository.getFavoritesCities(context)
            initial = false
        }
    }

}
package com.viniesantana.vsswheaterapp.feature.list

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.viniesantana.vsswheaterapp.Const
import com.viniesantana.vsswheaterapp.R
import com.viniesantana.vsswheaterapp.database.RoomManager
import com.viniesantana.vsswheaterapp.entity.City
import com.viniesantana.vsswheaterapp.entity.FavoriteCity
import com.viniesantana.vsswheaterapp.entity.FindResult
import kotlinx.android.synthetic.main.row_city_layout.*
import kotlinx.android.synthetic.main.row_city_layout.view.*

class ListAdapter(val callback: CityCallBack) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private var items : List<City>? = null
    private var favorites : List<FavoriteCity>? = null
    private var wind : String = Const.CELSIUS_WIND
    private var tempUnit : String = Const.CELSIUS_SHORT

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.row_city_layout, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = items?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items?.let {
            val city = it[position]

            holder.itemView.apply {

                if (city.weatherList.size > 0) {
                    tvCloudsState.text = city.weatherList[0].description

                    val icon = city.weatherList[0].icon
                    val url = "http://openweathermap.org/img/w/$icon.png"
                    Glide.with(this.context)
                        .load(url)
                        .placeholder(R.drawable.placeholder)
                        .into(imgIcon)
                }

                tvTemperature.text = city.main.temp.toInt().toString()
                tvTemperatureType.text = this@ListAdapter.tempUnit
                tvCityName.text = city.name
                tvOthersStates.text = "${resources.getString(R.string.wind)} ${city.wind.speed} ${this@ListAdapter.wind} | ${resources.getString(R.string.clouds)} ${city.clouds.all} % | ${city.main.pressure.toInt()} hpa"

                val favorite = this@ListAdapter.favorites?.find{
                    it.id == city.id
                }

                if(favorite == null) {
                    imgFavoriteTrue.visibility = View.GONE
                }else{
                    imgFavoriteTrue.visibility = View.VISIBLE
                }

                imgFavorite.setOnClickListener {
                    if(imgFavoriteTrue.isVisible) {
                        imgFavoriteTrue.visibility = View.GONE
                        callback.onItemClick(city, true)
                    }else{
                        imgFavoriteTrue.visibility = View.VISIBLE
                        callback.onItemClick(city, false)
                    }
                }
            }
        }
    }

    fun data(items: List<City>?, wind: String, tempUnit: String, favorites: List<FavoriteCity>?) {
        this.items = items
        this.wind = wind
        this.tempUnit = tempUnit
        this.favorites = favorites
        notifyDataSetChanged()
    }

    interface CityCallBack{
        fun onItemClick(city: City, delete : Boolean)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
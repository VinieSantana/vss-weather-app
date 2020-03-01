package com.viniesantana.vsswheaterapp.feature.list

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.viniesantana.vsswheaterapp.Const
import com.viniesantana.vsswheaterapp.R
import com.viniesantana.vsswheaterapp.entity.City
import com.viniesantana.vsswheaterapp.entity.FavoriteCity
import com.viniesantana.vsswheaterapp.feature.settings.SettingsActivity
import com.viniesantana.vsswheaterapp.utils.LocaleManager
import kotlinx.android.synthetic.main.activity_main.*
import androidx.lifecycle.ViewModelProviders
import com.viniesantana.vsswheaterapp.base.BaseActivity

class MainActivity : BaseActivity(), ListAdapter.CityCallBack {

    private val adapter = ListAdapter(this)
    private var tempUnit: String = Const.CELSIUS_SHORT
    private var windUnit: String = Const.CELSIUS_WIND
    private var favoriteCities: List<FavoriteCity>? = null

    val prefs by lazy {
        getSharedPreferences(Const.SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    val viewModel by lazy {
        ViewModelProviders.of(this).get(ListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViewUnits()
        initObservables()
        initUI()

    }

    override fun onRestart() {
        super.onRestart()

        if (viewModel.getTempUnit() != this.prefs.getString(Const.TEMPERATURE_UNIT, Const.CELSIUS) || viewModel.getLanguage() != this.prefs.getString(Const.LANGUAGE, LocaleManager.getCurrentLanguage(this))){
            viewModel.updateSearch(this)
            this.recreate()
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.list_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.menu_setting) {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initUI() {

        rvCities.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
        btnSearch.setOnClickListener {
            if (isDeviceConnected()) {
                progressBar.visibility = View.VISIBLE
                viewModel.findCity(this, edtCity.text.toString())
                progressBar.visibility = View.GONE
            }else{
                Toast.makeText(this, resources.getString(R.string.no_internet), Toast.LENGTH_LONG).show()
            }
        }
        progressBar.visibility = View.GONE
    }

    override fun onItemClick(city: City, delete: Boolean) {
        viewModel.changeFavorites(this, city, delete)
    }

    fun isDeviceConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnected()
    }

    private fun initObservables() {
        viewModel.setInitialPreferences( this.prefs.getString(Const.LANGUAGE, LocaleManager.getCurrentLanguage(this))!!, this.prefs.getString(Const.TEMPERATURE_UNIT, Const.CELSIUS)!!)
        if (isDeviceConnected()) {
            viewModel.initFavorites(this)
        }else{
            Toast.makeText(this, resources.getString(R.string.no_internet), Toast.LENGTH_LONG).show()
        }
        viewModel.observerList()?.observe(this, Observer {
            adapter.data(
                it,
                this.windUnit,
                this.tempUnit,
                favoriteCities
            )
        })

        viewModel.observerFavorites().observe(this, Observer {
            favoriteCities = it
        })

    }

    private fun setViewUnits() {
        if (prefs.getString(Const.TEMPERATURE_UNIT, Const.CELSIUS) == Const.CELSIUS) {
            this.tempUnit = Const.CELSIUS_SHORT
            this.windUnit = Const.CELSIUS_WIND
        } else {
            this.tempUnit = Const.FARENHEIGHT_SHORT
            this.windUnit = Const.FARENHEIGHT_WIND
        }
    }
}

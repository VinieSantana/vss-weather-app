package com.viniesantana.vsswheaterapp.feature.settings

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.viniesantana.vsswheaterapp.Const
import com.viniesantana.vsswheaterapp.R
import com.viniesantana.vsswheaterapp.utils.LocaleManager
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.*

class SettingsActivity : AppCompatActivity() {

    val prefs by lazy {
        getSharedPreferences(Const.SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initUI()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext( LocaleManager.setLocale(newBase!!))
    }
    private fun initUI() {

        val tempUnit = prefs.getString(
            Const.TEMPERATURE_UNIT,
            Const.CELSIUS
        )
        val language = prefs.getString(Const.LANGUAGE, LocaleManager.getCurrentLanguage(this))

        if (tempUnit == Const.CELSIUS) {
            rbCelcius.isChecked = true
        } else {
            rbFarenheight.isChecked = true
        }

        if (language == Const.ENGLISH) {
            rbEnglish.isChecked = true
        } else {
            rbPortuguese.isChecked = true
        }

        rgUnit.setOnCheckedChangeListener { radioGroup, radioButtonId ->

            if (radioButtonId == R.id.rbCelcius) {
                setPreferences(Const.TEMPERATURE_UNIT, Const.CELSIUS)
            } else {
                setPreferences(Const.TEMPERATURE_UNIT, Const.FARENHEIGHT)
            }
        }

        rgLanguage.setOnCheckedChangeListener { radioGroup, radioButtonId ->
            if (radioButtonId == R.id.rbEnglish) {
                setPreferences(Const.LANGUAGE, Const.ENGLISH)
                this.recreate()
            } else {
                setPreferences(Const.LANGUAGE, Const.PORTUGUESE)
                this.recreate()
            }
        }
    }

    private fun setPreferences(key: String, value: String) {
        prefs.edit().apply() {
            putString(key, value)
            apply()
        }
    }
}


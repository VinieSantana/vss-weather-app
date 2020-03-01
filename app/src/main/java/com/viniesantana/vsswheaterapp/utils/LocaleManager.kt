package com.viniesantana.vsswheaterapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import com.viniesantana.vsswheaterapp.Const
import java.util.*

object LocaleManager {

    fun setLocale(context : Context) : Context{
        Log.d("VINIELOC", Locale.getDefault().language.toString())
        return setNewLocale(context, getCurrentLanguage(context))
    }

    private fun setNewLocale(context : Context, language: String):Context {

        persistLanguage(context, language)
        return updateResources(context, language)
    }

    fun getCurrentLanguage(context : Context) = context.getSharedPreferences(Const.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Const.LANGUAGE, Locale.getDefault().language.toString())!!

    private fun persistLanguage(context : Context, language: String) {

    }

    private fun updateResources(context : Context, language: String): Context {
        var retContext = context
        val locale = Locale(language)
        Locale.setDefault(locale)

        val res = context.resources
        var config = Configuration(res.configuration).apply {
//            if (Build.VERSION.SDK_INT >= 17) {
                setLocale(locale);
                retContext = context.createConfigurationContext(this)
//            } else {
//                this.locale = locale;
//                res.updateConfiguration(this, res.displayMetrics)
//            }
        }
        return retContext
    }
}
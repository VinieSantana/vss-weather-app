package com.viniesantana.vsswheaterapp.base

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.viniesantana.vsswheaterapp.utils.LocaleManager

open class BaseActivity : AppCompatActivity(){

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleManager.setLocale(newBase!!))
    }
}
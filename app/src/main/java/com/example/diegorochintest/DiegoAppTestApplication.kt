package com.example.diegorochintest

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DiegoAppTestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}

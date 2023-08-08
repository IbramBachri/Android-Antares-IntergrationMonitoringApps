package com.antares.esp8266_sensor

import android.app.Application

class App : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder().build()
    }
}
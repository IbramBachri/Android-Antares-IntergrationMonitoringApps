package com.antares.esp8266_sensor

import com.antares.esp8266_sensor.antares.NetworkModule
import com.antares.esp8266_sensor.ui.MainActivity
import com.antares.esp8266_sensor.ui.ViewModelModule
import dagger.Component

@Component(
    modules = [
        NetworkModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {
    fun inject(activity: MainActivity)
}
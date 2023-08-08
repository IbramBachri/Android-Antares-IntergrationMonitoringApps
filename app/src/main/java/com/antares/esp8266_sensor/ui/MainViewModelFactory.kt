package com.antares.esp8266_sensor.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.antares.esp8266_sensor.antares.AntaresRepository
import com.google.gson.Gson

class MainViewModelFactory(
    private val antaresRepository: AntaresRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(antaresRepository, Gson()) as T
    }
}
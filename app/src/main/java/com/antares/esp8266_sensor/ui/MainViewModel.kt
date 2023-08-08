package com.antares.esp8266_sensor.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antares.esp8266_sensor.antares.AntaresContent
import com.antares.esp8266_sensor.antares.AntaresRepository
import com.antares.esp8266_sensor.antares.AntaresRequest
import com.antares.esp8266_sensor.antares.DeviceRequest
import com.antares.esp8266_sensor.antares.SwitchRequest
import com.antares.esp8266_sensor.antares.TemperatureContent
import com.antares.esp8266_sensor.antares.TemperatureRequest
import com.google.gson.Gson
import kotlinx.coroutines.launch

class MainViewModel(
    private val antaresRepository: AntaresRepository,
    private val gson: Gson
): ViewModel() {

    private val antaresEvent = MutableLiveData<AntaresContent?>()
    val antares = antaresEvent as LiveData<AntaresContent?>

    fun fetchData() {
        viewModelScope.launch {
            antaresEvent.postValue(antaresRepository.fetchData())
        }
    }

    fun updateSwitch(toggle: Boolean) {
        viewModelScope.launch {
            val switchRequest = SwitchRequest(toggle)
            val content = gson.toJson(switchRequest)
            antaresEvent.postValue(antaresRepository.updateSwitch(content))
        }
    }

    fun updateTemperature(temperature: Int) {
        viewModelScope.launch {
            val switchRequest = TemperatureRequest(temperature)
            val content = gson.toJson(switchRequest)
            antaresEvent.postValue(antaresRepository.updateTemperature(content))
        }
    }
}
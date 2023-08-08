package com.antares.esp8266_sensor.antares

import com.google.gson.annotations.SerializedName

data class AntaresRequest(
    @SerializedName("m2m:cin")
    val request: DeviceRequest
)

data class DeviceRequest(
    @SerializedName("con")
    val con: String
)

sealed interface ContentRequest

data class SwitchRequest(
    @SerializedName("switch_on_off")
    val switch: Boolean
): ContentRequest

data class TemperatureRequest(
    @SerializedName("setsuhu")
    val temperature: Int
): ContentRequest
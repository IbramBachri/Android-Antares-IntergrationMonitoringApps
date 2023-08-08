package com.antares.esp8266_sensor.antares

import com.google.gson.annotations.SerializedName

data class AntaresResponse(
    @SerializedName("m2m:list")
    val devices: List<AntaresDevice>
)

data class AntaresDevice(
    @SerializedName("m2m:cin")
    val data: AntaresData
)

data class AntaresData(
    @SerializedName("ct")
    val ct: String,

    @SerializedName("lt")
    val lt: String,

    @SerializedName("con")
    val con: String
)

data class AntaresContent(
    val countPeople: CountPeopleContent?,
    val temperature: TemperatureContent?,
    val switch: SwitchContent?
)

data class CountPeopleContent(
    @SerializedName("count")
    val count: Int
)

data class TemperatureContent(
    @SerializedName("temperature")
    val temperature: Float,

    @SerializedName("humidity")
    val humidity: Float,

    @SerializedName("temperature_ac")
    val temperatureAc: Float,

    @SerializedName("output_fuzzy")
    val outputFuzzy: Float
)

data class SwitchContent(
    @SerializedName("switch_on_off")
    val switch: Boolean
)
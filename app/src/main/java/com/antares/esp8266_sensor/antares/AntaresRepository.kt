package com.antares.esp8266_sensor.antares

import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class AntaresRepository(
    private val service: AntaresService,
    private val gson: Gson,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun fetchData(): AntaresContent? {
        return withContext(dispatcher) {
            try {
                val response = service.fetchData()
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    val responseData = responseBody.devices.map { it.data }

                    val takeCountPeopleData = responseData.firstOrNull { it.con.contains("count") }
                    val countPeopleData = if (takeCountPeopleData == null) {
                        null
                    } else {
                        gson.fromJson(takeCountPeopleData.con, CountPeopleContent::class.java)
                    }

                    val takeTemperatureData = responseData.firstOrNull { it.con.contains("temperature") }
                    val temperatureData = if (takeTemperatureData == null) {
                        null
                    } else {
                        gson.fromJson(takeTemperatureData.con, TemperatureContent::class.java)
                    }

                    val takeSwitchData = responseData.firstOrNull { it.con.contains("switch_on_off") }
                    val switchData = if (takeSwitchData == null) {
                        val switchContent = SwitchRequest(false)
                        val content = gson.toJson(switchContent)
                        updateSwitch(content)
                        SwitchContent(switchContent.switch)
                    } else {
                        gson.fromJson(takeSwitchData.con, SwitchContent::class.java)
                    }

                    AntaresContent(countPeopleData, temperatureData, switchData)
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun updateSwitch(content: String): AntaresContent? {
        return withContext(dispatcher) {
            try {
                val objectContent = JsonObject()
                objectContent.addProperty("con", content)

                val objectDevice = JsonObject()
                objectDevice.add("m2m:cin", objectContent)

                val body = objectDevice.toString().toRequestBody("application/json;ty=4".toMediaTypeOrNull())

                val response = service.updateSwitch(body)
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    val isSwitchData = responseBody.data.con.contains("switch_on_off")
                    val switchData = if (isSwitchData) {
                        gson.fromJson(responseBody.data.con, SwitchContent::class.java)
                    } else {
                        SwitchContent(false)
                    }

                    AntaresContent(null, null, switchData)
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun updateTemperature(content: String): AntaresContent? {
        return withContext(dispatcher) {
            try {
                val objectContent = JsonObject()
                objectContent.addProperty("con", content)

                val objectDevice = JsonObject()
                objectDevice.add("m2m:cin", objectContent)

                val body = objectDevice.toString().toRequestBody("application/json;ty=4".toMediaTypeOrNull())

                val response = service.updateTemperature(body)
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    val isTemperatureData = responseBody.data.con.contains("temperature")
                    val temperatureData = if (isTemperatureData) {
                        gson.fromJson(responseBody.data.con, TemperatureContent::class.java)
                    } else {
                        null
                    }
                    AntaresContent(null, temperatureData, null)
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }
}
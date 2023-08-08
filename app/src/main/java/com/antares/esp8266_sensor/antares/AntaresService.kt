package com.antares.esp8266_sensor.antares

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AntaresService {
    @GET("ESP8266_Sensor?ty=4&fu=1&drt=2")
    suspend fun fetchData(): Response<AntaresResponse>

    @POST("ESP8266_Sensor")
    suspend fun updateSwitch(@Body request: RequestBody): Response<AntaresDevice>

    @POST("ESP8266_Sensor")
    suspend fun updateTemperature(@Body request: RequestBody): Response<AntaresDevice>
}
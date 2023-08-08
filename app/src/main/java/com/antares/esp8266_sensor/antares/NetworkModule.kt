package com.antares.esp8266_sensor.antares

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {

    @Provides
    fun provideRetrofit(): Retrofit {
        val client = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-M2M-Origin", "YOUR ACCESS KEY ANTARES ACOUNT")
                .addHeader("Content-Type", "application/json;ty=4")
                .addHeader("Accept", "application/json")
                .build()
            chain.proceed(request)
        }).build()
        return Retrofit.Builder()
            .baseUrl("https://platform.antares.id:8443/~/antares-cse/antares-id/Monitoring_System/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideAntaresService(retrofit: Retrofit): AntaresService {
        return retrofit.create(AntaresService::class.java)
    }

    @Provides
    fun provideAntaresRepository(service: AntaresService): AntaresRepository {
        return AntaresRepository(service, Gson(),  Dispatchers.IO)
    }
}
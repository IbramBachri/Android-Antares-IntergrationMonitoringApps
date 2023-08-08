package com.antares.esp8266_sensor.ui

import com.antares.esp8266_sensor.antares.AntaresRepository
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {
    @Provides
    fun provideMainViewModelFactory(antaresRepository: AntaresRepository): MainViewModelFactory {
        return MainViewModelFactory(antaresRepository)
    }
}
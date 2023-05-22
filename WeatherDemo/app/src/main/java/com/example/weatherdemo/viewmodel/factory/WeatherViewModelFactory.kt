package com.example.weatherdemo.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherdemo.database.weather.WeatherRepository
import com.example.weatherdemo.viewmodel.PreviousWeathersViewModel
import java.lang.IllegalArgumentException

class WeatherViewModelFactory(
    private val repository: WeatherRepository,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("Unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PreviousWeathersViewModel::class.java)) {
            return PreviousWeathersViewModel(repository, application) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}
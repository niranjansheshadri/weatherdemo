package com.example.weatherdemo.viewmodel

import android.app.Application
import androidx.databinding.Observable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherdemo.database.weather.WeatherEntity
import com.example.weatherdemo.database.weather.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PreviousWeathersViewModel(private val repository: WeatherRepository, application: Application) :
    AndroidViewModel(application), Observable {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val weatherDetails = repository.weatherDetails

    init {
    }

    fun insertWeatherDetails(
        location: String, temperature: String,
        description: String, datetime: String
    ) {
        uiScope.launch {
            insert(WeatherEntity(0, location, temperature, description, datetime))
        }
    }

    private fun insert(weatherEntity: WeatherEntity): Job = viewModelScope.launch {
        repository.insert(weatherEntity)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }
}
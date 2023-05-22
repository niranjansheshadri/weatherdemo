package com.example.weatherdemo.database.weather

class WeatherRepository(private val dao: WeatherDatabaseDAO) {

    val weatherDetails = dao.getAllWeatherRecords()

    suspend fun insert(weatherDetails: WeatherEntity) {
        return dao.insert(weatherDetails)
    }
}
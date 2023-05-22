package com.example.weatherdemo.database.weather

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WeatherDatabaseDAO {

    @Insert
    suspend fun insert(weather: WeatherEntity)

    @Query("SELECT * FROM weather_details_table ORDER BY id DESC")
    fun getAllWeatherRecords(): LiveData<List<WeatherEntity>>

    @Query("DELETE FROM weather_details_table")
    suspend fun deleteAll(): Int
}
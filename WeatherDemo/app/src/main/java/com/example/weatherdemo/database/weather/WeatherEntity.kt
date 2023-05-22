package com.example.weatherdemo.database.weather

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_details_table")
data class WeatherEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "location")
    var location: String,

    @ColumnInfo(name = "temperature")
    var temperature: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "datetime")
    var datetime: String
)
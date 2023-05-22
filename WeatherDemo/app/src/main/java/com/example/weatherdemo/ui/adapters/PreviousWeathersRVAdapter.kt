package com.example.weatherdemo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherdemo.R
import com.example.weatherdemo.database.weather.WeatherEntity
import com.example.weatherdemo.databinding.ComponentPreviousWeathersBinding

class PreviousWeathersRVAdapter(private val weatherDetails: List<WeatherEntity>) :
    RecyclerView.Adapter<PreviousWeathersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviousWeathersViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ComponentPreviousWeathersBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.component_previous_weathers, parent, false
        )
        return PreviousWeathersViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return weatherDetails.size
    }

    override fun onBindViewHolder(holder: PreviousWeathersViewHolder, position: Int) {
        holder.bind(weatherDetails[position])
    }
}

class PreviousWeathersViewHolder(private val binding: ComponentPreviousWeathersBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(weatherEntity: WeatherEntity) {
        binding.tvFcwItemDescription.text = "Description: ".plus(weatherEntity.description)
        binding.tvFcwItemLocation.text = "Location: ".plus(weatherEntity.location)
        binding.tvFcwItemDateTime.text = "Date & Time: ".plus(weatherEntity.datetime)
        binding.tvFcwItemTemperature.text = weatherEntity.temperature
    }
}
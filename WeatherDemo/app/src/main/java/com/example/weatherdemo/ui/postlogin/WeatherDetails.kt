package com.example.weatherdemo.ui.postlogin

import com.example.weatherdemo.R
import com.example.weatherdemo.ui.adapters.ViewPagerAdapter
import com.example.weatherdemo.viewmodel.ViewPagerViewModel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.weatherdemo.databinding.ActivityWeatherDetailsBinding
import com.google.android.material.tabs.TabLayoutMediator

class WeatherDetails : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherDetailsBinding
    private lateinit var viewModel: ViewPagerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_weather_details)
        viewModel = ViewModelProvider(this).get(ViewPagerViewModel::class.java)
        binding.viewModel = viewModel

        setupViewPager()
    }

    private fun setupViewPager() {
        val viewPagerAdapter = ViewPagerAdapter(this)
        viewPagerAdapter.addFragment(CurrentWeatherFragment())
        viewPagerAdapter.addFragment(PreviousWeathersFragment())

        binding.viewpagerWeatherDetails.adapter = viewPagerAdapter

        TabLayoutMediator(
            binding.tabsWeatherDetails,
            binding.viewpagerWeatherDetails
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "Current Forecast"
                1 -> tab.text = "Previous Forecast"
            }
        }.attach()
    }

}
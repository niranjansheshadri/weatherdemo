package com.example.weatherdemo.viewmodel

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherdemo.BuildConfig
import com.example.weatherdemo.database.models.CurrentWeatherResponse
import com.example.weatherdemo.webapi.RetrofitClient
import kotlinx.coroutines.launch
import java.util.Locale

class CurrentWeatherViewModel() : ViewModel() {

    private val locationPermissionCode = 222

    val locationText = MutableLiveData<String>()
    val isViewVisible = ObservableField(false)

    private val _weatherData = MutableLiveData<CurrentWeatherResponse>()
    val weatherData: LiveData<CurrentWeatherResponse> get() = _weatherData

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(context: Context) {
        isViewVisible.set(false)

        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.requestSingleUpdate(
            LocationManager.GPS_PROVIDER, object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    val latitude = location.latitude
                    val longitude = location.longitude

                    val addresses: List<Address>?
                    val geocoder = Geocoder(context, Locale.getDefault())

                    addresses = geocoder.getFromLocation(
                        latitude,
                        longitude,
                        1
                    )

                    locationText.value =
                        addresses!![0].featureName.plus(", ").plus(addresses[0].countryName);

                    getWeatherData(latitude.toString(), longitude.toString(), "metric", context)
                }

                override fun onStatusChanged(
                    provider: String?,
                    status: Int,
                    extras: Bundle?
                ) {
                }

                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            },
            null
        )
    }

    fun getWeatherData(lat: String, log: String, units: String, context: Context) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.create()
                    .getWeatherData(lat, log, BuildConfig.WEATHER_APP_ID, units)
                if (response.isSuccessful) {
                    val data = response.body()
                    _weatherData.value = data!!

                    isViewVisible.set(true)
                } else {
                    // Handle error case
                }
            } catch (e: Exception) {
                // Handle network or other errors
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun checkLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestLocationPermission(fragment: Fragment) {
        fragment.requestPermissions(
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            locationPermissionCode
        )
    }

    fun handlePermissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ): Boolean {
        return if (requestCode == locationPermissionCode) {
            grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
    }
}
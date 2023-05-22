package com.example.weatherdemo.ui.postlogin

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherdemo.R
import com.example.weatherdemo.database.weather.WeatherDatabase
import com.example.weatherdemo.database.weather.WeatherRepository
import com.example.weatherdemo.databinding.FragmentCurrentWeatherBinding
import com.example.weatherdemo.utils.convertUnixTimestampToHumanReadable
import com.example.weatherdemo.utils.getCurrentDateTime
import com.example.weatherdemo.viewmodel.CurrentWeatherViewModel
import com.example.weatherdemo.viewmodel.PreviousWeathersViewModel
import com.example.weatherdemo.viewmodel.factory.WeatherViewModelFactory
import java.util.Calendar


class CurrentWeatherFragment : Fragment() {

    private lateinit var binding: FragmentCurrentWeatherBinding
    private lateinit var currentWeatherViewModel: CurrentWeatherViewModel
    private lateinit var previousWeathersViewModel: PreviousWeathersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_current_weather, container, false)
        binding.lifecycleOwner = this;

        currentWeatherViewModel =
            ViewModelProvider(requireActivity()).get(CurrentWeatherViewModel::class.java)
        binding.currentWeatherViewModel = currentWeatherViewModel

        val application = requireNotNull(this.activity).application
        val dao = WeatherDatabase.getInstance(application).weatherDatabaseDAO
        val repository = WeatherRepository(dao)
        val factory = WeatherViewModelFactory(repository, application)

        previousWeathersViewModel =
            ViewModelProvider(requireActivity(), factory).get(PreviousWeathersViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkLocationPermission(requireContext())
    }

    private fun checkLocationPermission(context: Context) {
        if (currentWeatherViewModel.checkLocationPermission(context)) {
            getCurrentLocation(requireContext())
        } else {
            currentWeatherViewModel.requestLocationPermission(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (currentWeatherViewModel.handlePermissionResult(
                requestCode,
                permissions,
                grantResults
            )
        ) {
            getCurrentLocation(requireContext())
        } else {
            val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setTitle("")
            alertDialogBuilder
                .setCancelable(false)
                .setMessage("You have denied some permissions. Allow all permissions at [Setting] > [Apps] > [Select Weather Demo] > [Permissions] > [Enable all Permissions]")
                .setPositiveButton(
                    "Go to settings"
                ) { dialog, id ->
                    dialog.dismiss()
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", requireContext().getPackageName(), null)
                    intent.data = uri
                    permissionActivityResultLauncher.launch(intent)
                }
                .setNegativeButton("No, Exit app") { dialog, id ->
                    dialog.dismiss()
                    requireActivity().finish()
                }
            val alertDialog: AlertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

    private fun getCurrentLocation(context: Context) {
        currentWeatherViewModel.getCurrentLocation(context)

        observeWeatherData()
    }

    private fun observeWeatherData() {
        currentWeatherViewModel.weatherData.observe(viewLifecycleOwner) { weatherData ->
            binding.tvFcwCurrentTemperature.text =
                weatherData.main.temp.toInt().toString().plus("° C")
            binding.tvFcwCurrentWeatherSummary.text = weatherData.weather.get(0).description
            binding.tvFcwFdSdSunrise.text =
                convertUnixTimestampToHumanReadable(weatherData.sys.sunrise).plus(" AM")
            binding.tvFcwFdSdSunset.text =
                convertUnixTimestampToHumanReadable(weatherData.sys.sunset).plus(" PM")

            binding.tvFcwFdWindSpeed.text =
                weatherData.wind.speed.toString().plus(" meter/sec")
            binding.tvFcwFdHumidity.text =
                weatherData.main.humidity.toString().plus(" %")

            if (weatherData.weather.get(0).main.equals("rain", ignoreCase = true)) {
                val drawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_rainny, null)
                binding.ivFcwCurrentWeatherIndicator.setImageDrawable(drawable)
            } else if (weatherData.weather.get(0).main.equals("clouds", ignoreCase = true)) {
                val drawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_cloudy, null)
                binding.ivFcwCurrentWeatherIndicator.setImageDrawable(drawable)
            } else {
                val calendar = Calendar.getInstance()
                val currentTime = calendar.get(Calendar.HOUR_OF_DAY)
                val drawable: Drawable?

                if (currentTime in 6..17) {
                    drawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_sunny, null)
                } else {
                    drawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_moon, null)
                }
                binding.ivFcwCurrentWeatherIndicator.setImageDrawable(drawable)
            }

            previousWeathersViewModel.insertWeatherDetails(
                currentWeatherViewModel.locationText.value.toString(),
                weatherData.main.temp.toInt().toString().plus("° C"),
                weatherData.weather.get(0).description, getCurrentDateTime()
            )
        }
    }

    var permissionActivityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult? ->
        if (currentWeatherViewModel.checkLocationPermission(requireContext())) {
            getCurrentLocation(requireContext())
        } else {
            currentWeatherViewModel.requestLocationPermission(this)
        }
    }
}
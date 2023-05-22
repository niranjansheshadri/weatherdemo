package com.example.weatherdemo.ui.postlogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherdemo.R
import com.example.weatherdemo.database.weather.WeatherDatabase
import com.example.weatherdemo.database.weather.WeatherRepository
import com.example.weatherdemo.databinding.FragmentPreviousWeathersBinding
import com.example.weatherdemo.ui.adapters.PreviousWeathersRVAdapter
import com.example.weatherdemo.viewmodel.PreviousWeathersViewModel
import com.example.weatherdemo.viewmodel.factory.WeatherViewModelFactory

class PreviousWeathersFragment : Fragment() {

    private lateinit var previousWeathersViewModel: PreviousWeathersViewModel
    private lateinit var binding: FragmentPreviousWeathersBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_previous_weathers, container, false
        )

        val application = requireNotNull(this.activity).application
        val dao = WeatherDatabase.getInstance(application).weatherDatabaseDAO
        val repository = WeatherRepository(dao)
        val factory = WeatherViewModelFactory(repository, application)
        previousWeathersViewModel =
            ViewModelProvider(this, factory).get(PreviousWeathersViewModel::class.java)

        binding.previousWeathers = previousWeathersViewModel
        binding.lifecycleOwner = this

        initRecyclerView()

        return binding.root

    }

    private fun initRecyclerView() {
        binding.rvFpwPreviousWeathers.layoutManager = LinearLayoutManager(this.context)
        displayUsersList()
    }

    private fun displayUsersList() {
        previousWeathersViewModel.weatherDetails.observe(viewLifecycleOwner, Observer {
            binding.rvFpwPreviousWeathers.adapter = PreviousWeathersRVAdapter(it)
        })
    }
}
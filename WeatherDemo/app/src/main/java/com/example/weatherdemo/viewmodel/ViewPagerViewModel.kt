package com.example.weatherdemo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherdemo.ui.adapters.ViewPagerAdapter

class ViewPagerViewModel : ViewModel() {
    val adapter = MutableLiveData<ViewPagerAdapter>()
    val onPageClicked = MutableLiveData<String>()

    // Other ViewModel logic

    fun onItemClicked(item: String) {
        onPageClicked.value = item
    }
}

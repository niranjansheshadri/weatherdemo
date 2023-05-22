package com.example.weatherdemo.viewmodel

import android.app.Application
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherdemo.database.register.RegisterRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: RegisterRepository, application: Application) :
    AndroidViewModel(application), Observable {

    @Bindable
    val inputUsername = MutableLiveData<String?>()

    @Bindable
    val inputPassword = MutableLiveData<String?>()

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigateToSignup = MutableLiveData<Boolean>()

    val navigateToSignup: LiveData<Boolean>
        get() = _navigateToSignup

    private val _navigateToUserDetails = MutableLiveData<Boolean>()

    val navigateToUserDetails: LiveData<Boolean>
        get() = _navigateToUserDetails

    private val _errorToast = MutableLiveData<Boolean>()

    val errorToast: LiveData<Boolean>
        get() = _errorToast

    private val _errorToastUsername = MutableLiveData<Boolean>()

    val erroToastUsername: LiveData<Boolean>
        get() = _errorToastUsername

    private val _errorToastInvalidPassword = MutableLiveData<Boolean>()

    val errorToastInvalidPassword: LiveData<Boolean>
        get() = _errorToastInvalidPassword


    fun signUp() {
        _navigateToSignup.value = true
    }

    fun doLogin() {
        if (inputUsername.value == null || inputPassword.value == null) {
            _errorToast.value = true
        } else {
            uiScope.launch {
                val usersNames = repository.getUserName(inputUsername.value!!)
                if (usersNames != null) {
                    if (usersNames.passwrd == inputPassword.value) {
                        inputUsername.value = null
                        inputPassword.value = null
                        _navigateToUserDetails.value = true
                    } else {
                        _errorToastInvalidPassword.value = true
                    }
                } else {
                    _errorToastUsername.value = true
                }
            }
        }
    }

    fun doNavigateToRegister() {
        _navigateToSignup.value = false
    }

    fun doneNavigatingUserDetails() {
        _navigateToUserDetails.value = false
    }


    fun toastError() {
        _errorToast.value = false
    }


    fun toastErrorUsername() {
        _errorToastUsername.value = false
    }

    fun toastInvalidPassword() {
        _errorToastInvalidPassword.value = false
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}
package com.example.weatherdemo.viewmodel

import android.app.Application
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.*
import com.example.weatherdemo.database.register.RegisterEntity
import com.example.weatherdemo.database.register.RegisterRepository
import kotlinx.coroutines.*


class RegisterViewModel(private val repository: RegisterRepository, application: Application) :
    AndroidViewModel(application), Observable {

    init {
    }

    var userDetailsLiveData = MutableLiveData<Array<RegisterEntity>>()

    @Bindable
    val inputFirstName = MutableLiveData<String?>()

    @Bindable
    val inputLastName = MutableLiveData<String?>()

    @Bindable
    val inputUsername = MutableLiveData<String?>()

    @Bindable
    val inputPassword = MutableLiveData<String?>()

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigateToLogin = MutableLiveData<Boolean>()

    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    private val _errorToast = MutableLiveData<Boolean>()

    val errorToast: LiveData<Boolean>
        get() = _errorToast

    private val _errorToastUsername = MutableLiveData<Boolean>()

    val errorToastUsername: LiveData<Boolean>
        get() = _errorToastUsername

    fun doRegisterUser() {
        if (inputFirstName.value == null || inputLastName.value == null || inputUsername.value == null || inputPassword.value == null) {
            _errorToast.value = true
        } else {
            uiScope.launch {
                val usersNames = repository.getUserName(inputUsername.value!!)
                if (usersNames != null) {
                    _errorToastUsername.value = true
                } else {
                    val firstName = inputFirstName.value!!
                    val lastName = inputLastName.value!!
                    val email = inputUsername.value!!
                    val password = inputPassword.value!!

                    insert(RegisterEntity(0, firstName, lastName, email, password))
                    inputFirstName.value = null
                    inputLastName.value = null
                    inputUsername.value = null
                    inputPassword.value = null
                    _navigateToLogin.value = true
                }
            }
        }
    }

    fun login() {
        _navigateToLogin.value = true
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun doneNavigating() {
        _navigateToLogin.value = false
    }

    fun toastError() {
        _errorToast.value = false
    }

    fun toastErrorUsername() {
        _errorToast.value = false
    }

    private fun insert(user: RegisterEntity): Job = viewModelScope.launch {
        repository.insert(user)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }
}






package com.example.weatherdemo.ui.prelogin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.weatherdemo.R
import com.example.weatherdemo.database.register.RegisterDatabase
import com.example.weatherdemo.database.register.RegisterRepository
import com.example.weatherdemo.databinding.FragmentLoginBinding
import com.example.weatherdemo.viewmodel.LoginViewModel
import com.example.weatherdemo.viewmodel.factory.LoginViewModelFactory


class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentLoginBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login, container, false
        )

        val application = requireNotNull(this.activity).application

        val dao = RegisterDatabase.getInstance(application).registerDatabaseDAO

        val repository = RegisterRepository(dao)

        val factory = LoginViewModelFactory(repository, application)

        loginViewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)

        binding.loginViewModel = loginViewModel

        binding.lifecycleOwner = this

        loginViewModel.navigateToSignup.observe(viewLifecycleOwner, Observer { hasFinished ->
            if (hasFinished == true) {
                navigateToSignUp()
                loginViewModel.doNavigateToRegister()
            }
        })

        loginViewModel.errorToast.observe(viewLifecycleOwner, Observer { hasError ->
            if (hasError == true) {
                Toast.makeText(
                    requireContext(),
                    "Please enter Username and Password.",
                    Toast.LENGTH_SHORT
                ).show()
                loginViewModel.toastError()
            }
        })

        loginViewModel.erroToastUsername.observe(viewLifecycleOwner, Observer { hasError ->
            if (hasError == true) {
                Toast.makeText(
                    requireContext(),
                    "User doesn't Exist. Please create an account use the app.",
                    Toast.LENGTH_SHORT
                ).show()
                loginViewModel.toastErrorUsername()
            }
        })

        loginViewModel.errorToastInvalidPassword.observe(viewLifecycleOwner, Observer { hasError ->
            if (hasError == true) {
                Toast.makeText(requireContext(), "Please enter valid Password.", Toast.LENGTH_SHORT)
                    .show()
                loginViewModel.toastInvalidPassword()
            }
        })

        loginViewModel.navigateToUserDetails.observe(viewLifecycleOwner, Observer { hasFinished ->
            if (hasFinished == true) {
                navigateToWeatherDetails()
                loginViewModel.doneNavigatingUserDetails()
            }
        })

        return binding.root
    }

    private fun navigateToSignUp() {
        val action = LoginFragmentDirections.actionLoginToSignup()
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun navigateToWeatherDetails() {
        val action = LoginFragmentDirections.actionLoginToWeatherDetails()
        NavHostFragment.findNavController(this).navigate(action)
    }
}
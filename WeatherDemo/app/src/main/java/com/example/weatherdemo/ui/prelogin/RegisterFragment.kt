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
import com.example.weatherdemo.databinding.FragmentRegisterBinding
import com.example.weatherdemo.viewmodel.RegisterViewModel
import com.example.weatherdemo.viewmodel.factory.RegisterViewModelFactory

class RegisterFragment : Fragment() {

    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentRegisterBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_register, container, false
        )

        val application = requireNotNull(this.activity).application

        val dao = RegisterDatabase.getInstance(application).registerDatabaseDAO

        val repository = RegisterRepository(dao)

        val factory = RegisterViewModelFactory(repository, application)

        registerViewModel = ViewModelProvider(this, factory).get(RegisterViewModel::class.java)

        binding.registerViewModel = registerViewModel

        binding.lifecycleOwner = this

        registerViewModel.navigateToLogin.observe(viewLifecycleOwner, Observer { hasFinished ->
            if (hasFinished == true) {
                navigateToLogin()
                registerViewModel.doneNavigating()
            }
        })

        registerViewModel.userDetailsLiveData.observe(viewLifecycleOwner, Observer {
        })


        registerViewModel.errorToast.observe(viewLifecycleOwner, Observer { hasError ->
            if (hasError == true) {
                Toast.makeText(requireContext(), "Please enter all the fields.", Toast.LENGTH_SHORT)
                    .show()
                registerViewModel.toastError()
            }
        })

        registerViewModel.errorToastUsername.observe(viewLifecycleOwner, Observer { hasError ->
            if (hasError == true) {
                Toast.makeText(requireContext(), "Username already exists.", Toast.LENGTH_SHORT)
                    .show()
                registerViewModel.toastErrorUsername()
            }
        })

        return binding.root
    }

    private fun navigateToLogin() {
        val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
        NavHostFragment.findNavController(this).navigate(action)
    }

}
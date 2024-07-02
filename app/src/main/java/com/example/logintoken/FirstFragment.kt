package com.example.logintoken

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.logintoken.databinding.ActivityMainBinding
import com.example.logintoken.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

        private lateinit var binding: FragmentFirstBinding
        private val viewModel: MyViewModel by viewModels()

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = FragmentFirstBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            observeViewModel()

            binding.loginButton.setOnClickListener {
                val username = binding.usernameEditText.text.toString().trim()
                val password = binding.passwordEditText.text.toString().trim()

                if (username.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.login(username, password)
                } else {
                    Toast.makeText(requireContext(), "Enter username and password", Toast.LENGTH_SHORT).show()
                }
            }

            binding.logoutButton.setOnClickListener {
                viewModel.logout()
            }
        }

        private fun observeViewModel() {
            viewModel.userResponse.observe(viewLifecycleOwner, Observer { userResponse ->
                userResponse?.let {
                    // Actualizar UI con datos del usuario
                    binding.userDataTextView.visibility = View.VISIBLE
                    binding.userDataTextView.text = "User Data:\nName: ${it.firstName}, Email: ${it.email}"
                }
            })

            viewModel.accountResponse.observe(viewLifecycleOwner, Observer { accountResponse ->
                accountResponse?.let {
                    // Actualizar UI con datos de la cuenta
                    binding.accountDataTextView.visibility = View.VISIBLE
                    val accountsInfo = it.joinToString("\n") { account ->
                        "Account ID: ${account.id}, Balance: ${account.money}"
                    }
                    binding.accountDataTextView.text = "Account Data:\n$accountsInfo"
                }
            })

            viewModel.error.observe(viewLifecycleOwner, Observer { error ->
                error?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            })

            viewModel.isLoggedIn.observe(viewLifecycleOwner, Observer { isLoggedIn ->
                if (isLoggedIn) {
                    // Usuario autenticado, mostrar datos y botón de logout
                    binding.userDataTextView.visibility = View.VISIBLE
                    binding.accountDataTextView.visibility = View.VISIBLE
                    binding.logoutButton.visibility = View.VISIBLE
                    binding.loginButton.visibility = View.GONE
                } else {
                    // Usuario no autenticado, ocultar datos y botón de logout
                    binding.userDataTextView.visibility = View.GONE
                    binding.accountDataTextView.visibility = View.GONE
                    binding.logoutButton.visibility = View.GONE
                    binding.loginButton.visibility = View.VISIBLE
                }
            })
        }
    }
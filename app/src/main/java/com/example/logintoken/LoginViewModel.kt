package com.example.logintoken

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.logintoken.Model.Remote.ApiRetrofit
import com.example.logintoken.Model.Remote.AuthManager
import com.example.logintoken.Model.Remote.RetrofitClient
import com.example.logintoken.Model.Response.AccountResponse
import com.example.logintoken.Model.Response.LoginRequest
import com.example.logintoken.Model.Response.LoginResponse
import com.example.logintoken.Model.Response.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MyViewModel(application: Application) : AndroidViewModel(application) {

    private val authManager: AuthManager = AuthManager(application.applicationContext)

    private val _userResponse = MutableLiveData<UserResponse?>()
    val userResponse: LiveData<UserResponse?>
        get() = _userResponse

    private val _accountResponse = MutableLiveData<List<AccountResponse>?>()
    val accountResponse: LiveData<List<AccountResponse>?>
        get() = _accountResponse

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean>
        get() = _isLoggedIn

    fun login(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val api = RetrofitClient.getApiService()
                val loginRequest = LoginRequest(username, password)
                val loginResponse = api.login(loginRequest)
                val authToken = loginResponse.accessToken
                authManager.saveToken(authToken)  // Aquí se guarda el token
                fetchUserByToken(authToken)
                fetchMyAccount(authToken)
                _isLoggedIn.postValue(true)
            } catch (e: Exception) {
                _error.postValue("Error de red: ${e.message}")
            }
        }
    }

    private suspend fun fetchUserByToken(token: String) {
        try {
            val api = RetrofitClient.getApiService(token)
            val userResponse = api.getUserByToken("Bearer $token")
            _userResponse.postValue(userResponse)
        } catch (e: Exception) {
            _error.postValue("Error al obtener datos de usuario: ${e.message}")
        }
    }

    private suspend fun fetchMyAccount(token: String) {
        try {
            val api = RetrofitClient.getApiService(token)
            val accountResponse = api.myAccount("Bearer $token")
            _accountResponse.postValue(accountResponse)
        } catch (e: Exception) {
            _error.postValue("Error al obtener datos de cuenta: ${e.message}")
        }
    }

    fun logout() {
        authManager.clearToken()
        _isLoggedIn.value = false
        _userResponse.value = null
        _accountResponse.value = null
    }
}
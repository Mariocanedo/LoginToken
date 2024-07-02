package com.example.logintoken.Model.Remote

import com.example.logintoken.Model.Response.AccountResponse
import com.example.logintoken.Model.Response.LoginRequest
import com.example.logintoken.Model.Response.LoginResponse
import com.example.logintoken.Model.Response.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiRetrofit {

    @Headers("Content-type:application/json")
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @GET("auth/me")
    suspend fun getUserByToken(@Header("Authorization") token: String): UserResponse

    @GET("accounts/me")
   suspend fun myAccount(@Header("Authorization") token: String): MutableList<AccountResponse>

}



package com.example.logintoken.Model.Remote

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Patron singleton
object RetrofitClient {
    private const val BASE_URL = "http://wallet-main.eba-ccwdurgr.us-east-1.elasticbeanstalk.com/"

    // Método para obtener una nueva instancia de Retrofit
    fun buildRetrofit(token: String? = null): Retrofit {
        val clientBuilder = OkHttpClient.Builder()

        token?.let {
            clientBuilder.addInterceptor(AuthInterceptor(it))
            Log.i("USUARIO", "Interceptor con token añadido: $it")
        }

        val client = clientBuilder.build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Método para obtener la API de Retrofit
    fun getApiService(token: String? = null): ApiRetrofit {
        return buildRetrofit(token).create(ApiRetrofit::class.java)
    }
}
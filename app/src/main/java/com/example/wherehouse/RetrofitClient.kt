package com.example.wherehouse
import com.example.wherehouse.Api
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private const val BASE_URL = "http://192.168.1.2:5000"

    private val gson = GsonBuilder()
        .setDateFormat("EEE, dd MMM yyyy HH:mm:ss z")
        .create()

    val apiService: Api by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(Api::class.java)
    }
}

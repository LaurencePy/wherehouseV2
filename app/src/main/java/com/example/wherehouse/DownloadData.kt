package com.example.wherehouse

import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

class DownloadData : AppCompatActivity() {
    private lateinit var spinner: Spinner

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.2:5000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()



}
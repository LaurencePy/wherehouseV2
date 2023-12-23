package com.example.wherehouse

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

class StockPrediction : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var imageView: ImageView
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.2:5000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    interface ApiService {
        @GET("{imageName}")
        fun getImage(@Path("imageName") imageName: String): Call<ResponseBody>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stock_prediction)

        spinner = findViewById(R.id.spinner)
        imageView = findViewById(R.id.imageView)

        val graphs = Array(100) { i -> "graph_${i + 1}.png" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, graphs)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedGraph = parent.getItemAtPosition(position).toString()
                loadImage(selectedGraph)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    private fun loadImage(imageName: String) {
        val service = retrofit.create(ApiService::class.java)
        val call = service.getImage(imageName)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        val inputStream = responseBody.byteStream()
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        imageView.setImageBitmap(bitmap)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }
        })
    }
}

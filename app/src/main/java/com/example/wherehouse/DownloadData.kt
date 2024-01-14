package com.example.wherehouse

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.wherehouse.RetrofitClient
import java.io.File
import java.io.FileOutputStream

class DownloadData : AppCompatActivity() {
    private lateinit var spinner: Spinner

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.2:5000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.download_data)

        spinner = findViewById(R.id.spinnerSelectTable)
        val tables = arrayOf("tblitems", "tbllocations", "tblsalesstatistics", "tblsales")
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tables)

        val downloadButton = findViewById<Button>(R.id.downloadButton)
        downloadButton.setOnClickListener {
            val selectedTable = spinner.selectedItem.toString()
            downloadData(selectedTable)
        }


    }

    private fun downloadData(table: String) {
        RetrofitClient.apiService.downloadData(table).enqueue(object : Callback<List<DataModel>> {
            override fun onResponse(call: Call<List<DataModel>>, response: Response<List<DataModel>>) {
                if (response.isSuccessful) {
                    val items = response.body()
                    data?.let {
                        saveToFile("$table.json", it)
                        Log.d("DownloadData", "Response: $data")

                    }
                } else {
                    val textView = findViewById<TextView>(R.id.responseView)
                    textView.text = "Error, please contact administrator"


                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {

                Log.e("DownloadData", "Error in downloadData", t)

                runOnUiThread {
                    val textView = findViewById<TextView>(R.id.responseView)
                    textView.text = "Error, please contact administrator"
                }
            }
        })
    }

    private fun saveToFile(fileName: String, data: String) {
        try {
            val file = File(
                getExternalFilesDir(null),
                fileName
            ) // This will save to the app-specific directory
            FileOutputStream(file).use { output ->
                output.write(data.toByteArray())
            }
            val textView = findViewById<TextView>(R.id.responseView)
            textView.text = "Successfully downloaded"
        } catch (e: Exception) {
            e.printStackTrace()
            val textView = findViewById<TextView>(R.id.responseView)
            textView.text = "Error, please contact administrator"
        }
    }

}
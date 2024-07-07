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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
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

        // populating the spinner with a choice of the tables to download
        val tables = arrayOf("tblitems", "tbllocations", "tblsalesstatistics", "tblsales")
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tables)

        val downloadButton = findViewById<Button>(R.id.downloadButton)

        downloadButton.setOnClickListener {
            val selectedTable = spinner.selectedItem.toString()
            downloadData(selectedTable)
        }


    }

    private fun downloadData(table: String) {
        RetrofitClient.apiService.downloadData(table).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val jsonData = response.body()?.string()
                    jsonData?.let {

                        // handling different data models as the tables have different columns of data
                        val dataModelType = when (table) {
                            "tblitems" -> object : TypeToken<List<DataModel>>() {}.type
                            "tblsales" -> object : TypeToken<List<DataModelTblSales>>() {}.type
                            "tbllocations" -> object : TypeToken<List<DataModelTblLocations>>() {}.type
                            "tblsalesstatistics" -> object : TypeToken<List<DataModelTblSalesStatistics>>() {}.type
                            else -> return
                        }
                        // calling the saveToFile function to download the files as .json
                        saveToFile("$table.json", it)
                    }
                } else {
                        val textView = findViewById<TextView>(R.id.responseView)
                        textView.text = "Error, please contact administrator"
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    val textView = findViewById<TextView>(R.id.responseView)
                    textView.text = "Error, please contact administrator"
            }
        })
    }

    private fun saveToFile(fileName: String, data: String) {
        try {

            // gets the directory for the wherehouse app to place files there
            val file = File(getExternalFilesDir(null), fileName)
            // writes the data to the file
            file.writeText(data)

            val textView = findViewById<TextView>(R.id.responseView)
            textView.text = "Successfully downloaded!"

        } catch (error: Exception) {
            val textView = findViewById<TextView>(R.id.responseView)
            textView.text = "Error, please contact administrator"
        }
    }
}


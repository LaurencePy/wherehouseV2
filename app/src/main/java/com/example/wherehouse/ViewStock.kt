package com.example.wherehouse

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class ViewStock : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_stock)

        val listView: ListView = findViewById(R.id.listView)

        val call: Call<List<DataModel>> = RetrofitClient.apiService.getTblItems()

        call.enqueue(object : Callback<List<DataModel>> {
            override fun onResponse(call: Call<List<DataModel>>, response: Response<List<DataModel>>) {
                if (response.isSuccessful) {
                    val data: List<DataModel>? = response.body()
                    data?.let {
                        Log.d("API_RESPONSE", "Response: $data")

                        val adapter = ListAdapter(this@ViewStock, R.layout.list_item_layout, it)
                        listView.adapter = adapter
                    }
                } else {
                    Log.e("API_RESPONSE", "API call failed with code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<DataModel>>, t: Throwable) {
                try {
                    val errorResponse = (t as HttpException).response()?.errorBody()?.string()
                    Log.e("API_RESPONSE", "API call failed: $errorResponse")
                } catch (e: Exception) {
                    Log.e("API_RESPONSE", "API call failed: ${t.message}")
                }
            }
        })
    }
}

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

        // API call using my RetrofitClient object to fetch tblitems data from the server
        val call: Call<List<DataModel>> = RetrofitClient.apiService.getTblItems()

        /* Asynchronous API call to keep the app running while the call
        is in progress alternatively to a synchronous API call
         */
        call.enqueue(object : Callback<List<DataModel>> {

            // when the API call is successful
            override fun onResponse(call: Call<List<DataModel>>, response: Response<List<DataModel>>) {
                if (response.isSuccessful) {
                    // Accessing the data received from the API response
                    val data: List<DataModel>? = response.body()

                    // error checking that the data is not null
                    data?.let {
                        // Logging the API's data response for me to test the data response
                        Log.d("API_RESPONSE", "Response: $data")

                        // Creating and setting up the adapter to display the data in the ListView
                        val adapter = ListAdapter(this@ViewStock, R.layout.list_item_layout, it)
                        listView.adapter = adapter
                    }
                } else {
                    // Logs the error code to the Logcat panel so that I can identify errors and debug the program
                    Log.e("API_RESPONSE", "API call failed with code ${response.code()}")
                }
            }

            // when the API call is unsuccessful, I implemented more error handling and error logging for debugging uses
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

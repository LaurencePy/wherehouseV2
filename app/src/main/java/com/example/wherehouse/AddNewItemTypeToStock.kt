package com.example.wherehouse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class AddNewItemTypeToStock : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_new_item_type_to_stock)

        val newItemId = findViewById<EditText>(R.id.itemIdInput)
        val newItemName = findViewById<EditText>(R.id.itemNameInput)
        val newExpiryDate = findViewById<EditText>(R.id.expiryDateInput)
        val newQuantity = findViewById<EditText>(R.id.quantityInput)
        val submission = findViewById<Button>(R.id.submitButton)

        submission.setOnClickListener {
            val item = DataModel(
                itemId = newItemId.text.toString().toInt(),
                itemName = newItemName.text.toString(),
                expiryDate = newExpiryDate.text.toString(),
                quantity = newQuantity.text.toString().toInt()
            )
            addItemToStock(item)
        }
    }

    private fun addItemToStock(item: DataModel) {
        val apiService = RetrofitClient.apiService
        val responseTextView = findViewById<TextView>(R.id.responseView)

        apiService.addItem(item).enqueue(object : Callback<Responses> {
            override fun onResponse(
                call: Call<Responses>,
                response: retrofit2.Response<Responses>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        Log.d("API RESPONSE", "Response:${response.body()}")
                        responseTextView.text = "Success:${response.body()}"
                    } else {
                        Log.e("API RESPONSE", "null")
                        responseTextView.text = "null"
                    }
                } else {
                    Log.e("API RESPONSE", "fail: ${response.code()}")
                    responseTextView.text = "fail: ${response.code()}"
                }
            }
            override fun onFailure(call: Call<Responses>, t: Throwable) {
                try {
                    val errorResponse = (t as HttpException).response()?.errorBody()?.string()
                    Log.e("API RESPONSE", "error: $errorResponse")
                } catch (e: Exception) {
                    Log.e("API RESPONSE", "error: ${t.message}")
                }
            }
        })
    }
}




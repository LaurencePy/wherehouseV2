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

    private lateinit var responseTextView: TextView
    private val apiService = RetrofitClient.apiService // Assuming RetrofitClient is properly set up

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_new_item_type_to_stock)

        val newItemId = findViewById<EditText>(R.id.itemIdInput)
        val newItemName = findViewById<EditText>(R.id.itemNameInput)
        val newExpiryDate = findViewById<EditText>(R.id.expiryDateInput)
        val newQuantity = findViewById<EditText>(R.id.quantityInput)
        val newLocation = findViewById<EditText>(R.id.locationInput)
        val submission = findViewById<Button>(R.id.submitButton)
        responseTextView = findViewById<TextView>(R.id.responseView)

        submission.setOnClickListener {
            val ItemIDInteger = newItemId.text.toString().toIntOrNull()
            val quantityString = newQuantity.text.toString()
            val itemNameString = newItemName.text.toString()
            val locationString = newLocation.text.toString()
            val expiryDateString = newExpiryDate.text.toString()

            // YYYY-MM-DD format
            val dateRegex = Regex("^\\d{4}-(0?[1-9]|1[0-2])-(0?[1-9]|[12]\\d|3[01])$")

            // Must be a letter followed by a single number
            val locationRegex = Regex("[A-Za-z][0-9]")

            val quantityInt = quantityString.toIntOrNull()

            if (ItemIDInteger == null ||
                itemNameString.isEmpty() ||
                itemNameString.toIntOrNull() != null ||
                itemNameString.toFloatOrNull() != null ||
                !expiryDateString.matches(dateRegex) ||
                quantityInt == null ||
                !locationString.matches(locationRegex) ||
                locationString.isEmpty()) {
                responseTextView.text = "Error, incorrect input"
                return@setOnClickListener
            }

            val item = DataModel(
                itemId = ItemIDInteger,
                itemName = itemNameString,
                expiryDate = expiryDateString,
                quantity = quantityInt,
                location = locationString
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
                        Log.d("API RESPONSE", "Response")
                        responseTextView.text = "Success"
                    } else {
                        Log.e("API RESPONSE", "null")
                        responseTextView.text = "null"
                    }
                } else {
                    Log.e("API RESPONSE", "fail")
                    responseTextView.text = "fail"
                }
            }
            override fun onFailure(call: Call<Responses>, t: Throwable) {
                try {
                    responseTextView.text = "error, please contact administrator"
                } catch (e: Exception) {
                    responseTextView.text = "error, please contact administrator"
                }
            }
        })
    }
}




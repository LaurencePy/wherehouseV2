package com.example.wherehouse

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddNewItemTypeToStock : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_new_item_type_to_stock)

        val editTextItemId = findViewById<EditText>(R.id.itemIdInput)
        val editTextItemName = findViewById<EditText>(R.id.itemNameInput)
        val editTextExpiryDate = findViewById<EditText>(R.id.expiryDateInput)
        val buttonAddItem = findViewById<Button>(R.id.submitButton)

        buttonAddItem.setOnClickListener {
            val item = DataModel(
                itemId = editTextItemId.text.toString().toInt(),
                itemName = editTextItemName.text.toString(),
                expiryDate = editTextExpiryDate.text.toString()
            )
            addItemToStock(item)
        }
    }

    private fun addItemToStock(item: DataModel) {
        val apiService = RetrofitClient.apiService
        apiService.addItem(item).enqueue(object : Callback<DataModel> {
            override fun onResponse(call: Call<DataModel>, response: Response<DataModel>) {

            }

            override fun onFailure(call: Call<DataModel>, t: Throwable) {

            }
        })
    }
}


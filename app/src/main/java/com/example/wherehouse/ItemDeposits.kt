package com.example.wherehouse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ItemDeposits : AppCompatActivity() {

    private lateinit var selectItemID: Spinner
    private lateinit var editQuantity: EditText
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.2:5000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_deposits)

        selectItemID = findViewById(R.id.spinner)
        editQuantity = findViewById(R.id.editQuantity)
        val submitButton: Button = findViewById(R.id.submitButton)
        val scanBarcodeButton: Button = findViewById(R.id.scanBarcodeButton)

        val items = Array(100) { i -> "item id:${i + 1}" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        selectItemID.adapter = adapter

        submitButton.setOnClickListener {
            val ItemID = selectItemID.selectedItem.toString().substringAfterLast(':')
            val addToQuantity = editQuantity.text.toString()
            updateItemQuantity(ItemID, addToQuantity)
        }

        scanBarcodeButton.setOnClickListener {
            val integrator = IntentIntegrator(this@ItemDeposits)
            integrator.setDesiredBarcodeFormats(listOf(IntentIntegrator.CODE_39))
            integrator.setPrompt("Scan a barcode")
            integrator.setCameraId(0)
            integrator.setBeepEnabled(false)
            integrator.setBarcodeImageEnabled(true)
            integrator.initiateScan()
        }
    }

    private fun updateItemQuantity(ItemID: String, additionalQuantity: String) {
        val apiService = retrofit.create(Api::class.java) // Create API service instance

        val ItemIDInteger = ItemID.toIntOrNull() ?: return
        val addToQuantityInteger = additionalQuantity.toIntOrNull() ?: return

        val updateRequest = AlterQuantityModel(ItemIDInteger, addToQuantityInteger)

        apiService.updateItemQuantity(updateRequest).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // Handle success - Notify the user that the quantity has been updated
                    Toast.makeText(this@ItemDeposits, "Quantity updated successfully", Toast.LENGTH_LONG).show()
                } else {
                    // Handle API response failure - Log the error and notify the user
                    Log.e("API_ERROR", "Response error: ${response.errorBody()?.string()}")
                    Toast.makeText(this@ItemDeposits, "Failed to update quantity", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Handle API call failure - Log the error and notify the user
                Log.e("API_ERROR", "Call failed: ${t.message}")
                Toast.makeText(this@ItemDeposits, "API call failed: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result.contents != null) {
                val scannedItemID = result.contents
                setSpinnerValue(scannedItemID)
            }
        }
    }

    private fun setSpinnerValue(ItemID: String) {
        val position = (ItemID.toIntOrNull() ?: 0) - 1
        if (position in 0 until selectItemID.count) {
            selectItemID.setSelection(position)
        }
    }
}

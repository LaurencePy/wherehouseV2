package com.example.wherehouse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
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

class ItemWithdrawals : AppCompatActivity() {

    private lateinit var selectItemID: Spinner
    private lateinit var editQuantity: EditText
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.2:5000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_withdrawals)

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
            removeFromQuantity(ItemID, addToQuantity)
        }

        scanBarcodeButton.setOnClickListener {
            val integrator = IntentIntegrator(this@ItemWithdrawals)
            integrator.setDesiredBarcodeFormats(listOf(IntentIntegrator.CODE_39))
            integrator.setPrompt("Scan a barcode")
            integrator.setCameraId(0)
            integrator.setBeepEnabled(false)
            integrator.setBarcodeImageEnabled(true)
            integrator.initiateScan()
        }
    }

    private fun removeFromQuantity(ItemID: String, additionalQuantity: String) {
        val apiService = retrofit.create(Api::class.java)
        /* converting values to integers in a seperate variable
        as I was having issues where it was required as a string
         */
        val ItemIDInteger = ItemID.toIntOrNull() ?: return
        val removeFromQuantityInteger = additionalQuantity.toIntOrNull() ?: return
        val responseTextView = findViewById<TextView>(R.id.responseView)

        if (ItemIDInteger == null || removeFromQuantityInteger == null) {
            responseTextView.text = "Error, incorrect input"
            return
        }
        val updateRequest = AlterQuantityModel(ItemIDInteger, removeFromQuantityInteger)

        apiService.removeFromQuantity(updateRequest).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    responseTextView.text = "Success!"
                } else {
                    responseTextView.text = ":( Error, please contact administrator"
                    Log.e("ERROR", "Response: ${response.errorBody()?.string()}")

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Handle API call failure - Log the error and notify the user
                Log.e("ERROR", "Call failed: ${t.message}")
                responseTextView.text = ":( Error, please contact administrator"
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

package com.example.wherehouse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EditStock : AppCompatActivity() {

    private lateinit var selectItemID: Spinner
    private lateinit var editItemName: EditText
    private lateinit var editExpiryDate: EditText
    private lateinit var editQuantity: EditText
    private lateinit var editLocation: EditText
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.2:5000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_stock)

        selectItemID = findViewById(R.id.spinner)
        editItemName = findViewById(R.id.editItemName)
        editExpiryDate = findViewById(R.id.editExpiryDate)
        editQuantity = findViewById(R.id.editQuantity)
        editLocation = findViewById(R.id.editLocation)
        val submitButton: Button = findViewById(R.id.submitButton)
        val scanBarcodeButton: Button = findViewById(R.id.scanBarcodeButton)

        val items = Array(100) { i -> "item id:${i + 1}" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        selectItemID.adapter = adapter

        submitButton.setOnClickListener {
            val ItemID = selectItemID.selectedItem.toString().substringAfterLast(':')
            updateItemDetails(ItemID)
        }

        scanBarcodeButton.setOnClickListener {
            val integrator = IntentIntegrator(this@EditStock)
            integrator.setDesiredBarcodeFormats(listOf(IntentIntegrator.CODE_39))
            integrator.setPrompt("Scan a barcode")
            integrator.setCameraId(0)
            integrator.setBeepEnabled(false)
            integrator.setBarcodeImageEnabled(true)
            integrator.initiateScan()
        }
    }

    private fun updateItemDetails(ItemID: String) {
        val apiService = retrofit.create(Api::class.java)

        /*
        added values/constants to account for
        the extra data that is being altered
         */

        val ItemIDInteger = ItemID.toIntOrNull() ?: return
        val itemName = editItemName.text.toString()
        val expiryDate = editExpiryDate.text.toString()
        val quantity = editQuantity.text.toString().toIntOrNull() ?: return
        val location = editLocation.text.toString()
        val responseTextView = findViewById<TextView>(R.id.responseView)

        // see document for regex credit

        //YYYY-MM-DD format
        val dateRegex = Regex("^\\d{4}-(0?[1-9]|1[0-2])-(0?[1-9]|[12]\\d|3[01])$")

        //Must be a letter followed by a single number
        val locationRegex = Regex("[A-Za-z][0-9]")

        val quantityString = editQuantity.text.toString()

        if (ItemIDInteger == null ||
            itemName.isEmpty() ||
            itemName.toIntOrNull() != null ||
            itemName.toFloatOrNull() != null ||
            !expiryDate.matches(dateRegex) ||
            quantityString.toIntOrNull() == null ||
            !location.matches(locationRegex) ||
            location.isEmpty()) {
            responseTextView.text = "Error, incorrect input"
            return
        }



        val updateRequest = DataModel(ItemIDInteger, itemName, expiryDate, quantity, location)

        apiService.updateItem(updateRequest).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    responseTextView.text = "Success!"
                } else {
                    responseTextView.text = ":( Error, please contact administrator"
                    Log.e("ERROR", "Response: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Handle API call failure
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

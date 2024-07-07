package com.example.wherehouse
import com.google.gson.annotations.SerializedName
data class DataModelTblSales (

        @SerializedName("ItemID")
        val itemId: Int,
        val saleDate: String,
        val salePrice: Float,
        val quantitySold: Int
        )
package com.example.wherehouse
import com.google.gson.annotations.SerializedName
data class AlterQuantityModel(
    @SerializedName("ItemID")
    val ItemID: Int,

    val addToQuantity: Int
)
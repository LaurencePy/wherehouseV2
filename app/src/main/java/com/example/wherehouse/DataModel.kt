package com.example.wherehouse
import com.google.gson.annotations.SerializedName

data class DataModel(
    @SerializedName("itemid")
    val itemId: Int,

    @SerializedName("itemname")
    val itemName: String,

    @SerializedName("expirydate")
    val expiryDate: String
)

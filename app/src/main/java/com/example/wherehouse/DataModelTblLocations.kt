package com.example.wherehouse
import com.google.gson.annotations.SerializedName
data class DataModelTblLocations (

    @SerializedName("ItemID")
    val itemId: Int,
    val location: String
)
package com.example.wherehouse

import com.google.gson.annotations.SerializedName

data class DataModelTblSalesStatistics (
    @SerializedName("ItemID")
    val itemId: Int,
    val salesWeek: Int,
    val salesMonth: Int,
    val salesYear: Int
    )
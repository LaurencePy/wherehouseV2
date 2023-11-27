package com.example.wherehouse

import com.example.wherehouse.DataModel
import retrofit2.Call
import retrofit2.http.GET

interface Api {
    @GET("/get_tblitems")
    fun getTblItems(): Call<List<DataModel>>
}

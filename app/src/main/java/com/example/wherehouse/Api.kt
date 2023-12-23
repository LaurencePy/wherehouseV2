package com.example.wherehouse

import com.example.wherehouse.DataModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {
    @GET("/get_tblitems")
    fun getTblItems(): Call<List<DataModel>>

    @GET("{imageName}")
    fun getImage(@Path("imageName") imageName: String): Call<ResponseBody>
}

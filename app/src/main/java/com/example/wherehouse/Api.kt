package com.example.wherehouse

import android.content.ClipData
import com.example.wherehouse.DataModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.POST
interface Api {
    @GET("/get_tblitems")
    fun getTblItems(): Call<List<DataModel>>

    @GET("{imageName}")
    fun getImage(@Path("imageName") imageName: String): Call<ResponseBody>


    @GET("/get_{table}")
    fun downloadData(@Path("table") table: String): Call<ResponseBody>


    @POST("/add_item")
    fun addItem(@Body newItem: DataModel): Call<Responses>


    @POST("/add_to_quantity")
    fun updateItemQuantity(@Body request: AlterQuantityModel): Call<ResponseBody>

    @POST("/remove_from_quantity")
    fun removeFromQuantity(@Body request: AlterQuantityModel): Call<ResponseBody>

    @POST("/edit_item")
    fun updateItem(@Body request: DataModel): Call<ResponseBody>

}

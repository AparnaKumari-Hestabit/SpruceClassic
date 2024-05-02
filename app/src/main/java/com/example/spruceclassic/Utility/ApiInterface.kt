package com.example.spruceclassic.Utility

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("posts")
    fun getData(@Query("per_page") perPage: Int, @Query("page") page: Int): Call<List<DataModel>>

}
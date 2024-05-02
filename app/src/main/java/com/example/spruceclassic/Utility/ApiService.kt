package com.example.spruceclassic.Utility

import com.example.spruceclassic.CategoryResponse
import com.example.spruceclassic.subscreens.PostResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {
    @GET("posts")
//    fun getData(@Query("per_page") perPage: Int, @Query("page") page: Int): Call<List<PostResponse>>
    fun getData(): Call<List<PostResponse>>

//hit api for the url from above api
    @GET
    fun getMedia(@Url mediaUrl: String): Call<MediaResponse>


    @GET("categories")
    fun getCategory(): Call<List<CategoryResponse>>


    companion object {
        private const val BASE_URL = "https://spruce.hestawork.com/wp-json/wp/v2/"
        private const val PER_PAGE = 5

        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}

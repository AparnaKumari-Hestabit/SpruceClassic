package com.example.spruceclassic.subscreens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.spruceclassic.R
import com.example.spruceclassic.Utility.ApiService
import com.example.spruceclassic.Utility.MediaResponse
import com.example.spruceclassic.navigation.Screens
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun CategoryScreen(a: String?, b: String?, navController: NavController){

    var posts by remember { mutableStateOf<List<Post>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val id = b?.toInt()

    LaunchedEffect(Unit) {
        callPosts(id) { fetchedPosts ->
            // Update the list of posts
            posts = fetchedPosts
            isLoading = false
        }
    }

    // Display a progress bar while loading
    if (isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    } else {
        // Display the list of posts
        posts?.let { postList ->
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    if (a != null) {
                        Text(
                            text = a,
                            fontSize = 22.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                }
                items(postList) { post ->
                    PojoListItem(post = post, navController)
                }
            }
        }
    }
}

private fun callPosts(id:Int?,onSuccess: (List<Post>) -> Unit) {
    val apiService = ApiService.create()
    val call: Call<List<PostResponse>> = apiService.getData()

    call.enqueue(object : Callback<List<PostResponse>> {
        override fun onResponse(call: Call<List<PostResponse>>, response: Response<List<PostResponse>>) {
            if (response.isSuccessful) {
                val postsResponse: List<PostResponse>? = response.body()
                Log.e("TAG", "Response successful for category: ${response.body()}")

                val posts = mutableListOf<Post>()

                postsResponse?.forEach { postResponse ->

                        val featuredMediaHref = postResponse.links?.featuredMedia?.get(0)?.href ?: ""
                        fetchMediaId(featuredMediaHref) { mediaUrl ->

                            val categories = postResponse.categories
                            Log.e("TAG ", "list of categories: $categories")
                            if (categories.contains(id)) {
                                val post = Post(
                                    id = postResponse.id,
                                    date = postResponse.date,
                                    title = postResponse.title.rendered,
                                    content = postResponse.content.rendered,
                                    excerpt = postResponse.excerpt.rendered,
                                    imageUrl = mediaUrl
                                )
                                posts.add(post)
                            }

                            Log.e("TAG ", "featured edos url: $mediaUrl")

                            if (posts.size == postsResponse.size) {
                                // All posts have been processed, invoke onSuccess
                                onSuccess(posts)
                            }
                        }
                }
            } else {
                Log.e("TAG", "Response unsuccessful: ${response.errorBody()?.string()}")
            }
        }

        override fun onFailure(call: Call<List<PostResponse>>, t: Throwable) {
            Log.e("TAG", "API call failed for category: $t")
        }
    })
}

private fun fetchMediaId(mediaLink: String, callback: (String?) -> Unit) {
    val apiService = ApiService.create()
    val call: Call<MediaResponse> = apiService.getMedia(mediaLink)

    call.enqueue(object : Callback<MediaResponse> {
        override fun onResponse(call: Call<MediaResponse>, response: Response<MediaResponse>) {
            if (response.isSuccessful) {
                val mediaResponse: MediaResponse? = response.body()
                val mediaId = mediaResponse?.url

                Log.e("TAG", "Media API call successful: ${mediaId}")


                callback(mediaId)
            } else {
                callback(null)
                Log.e("TAG", "Media API call unsuccessful: ${response.errorBody()?.string()}")
            }
        }

        override fun onFailure(call: Call<MediaResponse>, t: Throwable) {

            callback(null)
            Log.e("TAG", "Media API call failed: $t")
        }
    })
}




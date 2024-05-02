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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HomeScreen(navController: NavController) {

    var posts by remember { mutableStateOf<List<Post>?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Call the API and update the list of posts
    LaunchedEffect(Unit) {
        callAPI { fetchedPosts ->
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
    }
    else {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Skip the first two items and show the next three
            items(posts!!.drop(2).take(3)) { post ->
                showTopLayout(post)
            }
            item {
                Text(
                    text = "Featured Post",
                    fontSize = 22.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
            items(posts!!) { post ->
                Log.e("TGRF", " listdfgdfj : $posts")
                PojoListItem(post = post, navController)
            }
        }
    }
}



private fun callAPI(onSuccess: (List<Post>) -> Unit) {
    val apiService = ApiService.create()
    val call: Call<List<PostResponse>> = apiService.getData()

    call.enqueue(object : Callback<List<PostResponse>> {
        override fun onResponse(call: Call<List<PostResponse>>, response: Response<List<PostResponse>>) {
            if (response.isSuccessful) {
                val postsResponse: List<PostResponse>? = response.body()
                Log.e("TAG", "Response successful: ${response.body()}")

                val posts = mutableListOf<Post>()

                postsResponse?.forEach { postResponse ->
                    val featuredMediaHref = postResponse.links?.featuredMedia?.get(0)?.href ?: ""
                    fetchMediaId(featuredMediaHref) { mediaUrl ->
                        val post = Post(
                            id = postResponse.id,
                            date = postResponse.date,
                            title = postResponse.title.rendered,
                            content = postResponse.content.rendered,
                            excerpt = postResponse.excerpt.rendered,
                            imageUrl = mediaUrl
                        )
                        posts.add(post)


                        Log.e("TAG ", "featured edos url: $mediaUrl")

                        if (posts.size == postsResponse.size) {
                            // All posts have been processed, invoke onSuccess
                            handlePosts(posts)
                            onSuccess(posts)
                        }
                    }
                }
            } else {
                Log.e("TAG", "Response unsuccessful: ${response.errorBody()?.string()}")
            }
        }

        override fun onFailure(call: Call<List<PostResponse>>, t: Throwable) {
            Log.e("TAG", "API call failed: $t")
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


// Handle the list of posts
private fun handlePosts(posts: List<com.example.spruceclassic.subscreens.Post>) {
    // Implement your logic to handle the list of posts
}


// Define your data classes (Post, PostResponse, Title, Content, Links)
data class Post(
    val id: Int,
    val date: String,
    val title: String,
    val content: String,
    val excerpt: String,
    val imageUrl: String?
)

data class PostResponse(
    val id: Int,
    val date: String,
    val title: Title,
    val content: Content,
    val excerpt: Excerpt,
    val imageUrl: String?,
    @SerializedName("_links")
    val links: Links?,
    @SerializedName("categories")
    val categories: List<Int>
)

data class Title(
    val rendered: String
)

data class Content(
    val rendered: String
)

data class Excerpt(
    val rendered: String,
    val protected: Boolean
)

data class Links(
    @SerializedName("wp:featuredmedia")
    val featuredMedia: List<FeaturedMediaLink>?
)

data class FeaturedMediaLink(
    val href: String
)



@Composable
fun PojoListItem(post: Post, navController: NavController) {

//    val post1 = com.example.spruceclassic.Utility.Post(post.id, post.date, post.title, post.content, post.excerpt)

    Log.e("TA G" , "image data : ${post.imageUrl}" )

    //Featured-Post Card
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier.padding(0.dp, 5.dp)
    ) {
        Column {

            Column {

                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
//                        painter = painterResource(id = R.drawable.sample_pic),
                        painter = rememberAsyncImagePainter(post.imageUrl),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.FillBounds
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp, 8.dp)
                    ) {
                        Card(
                            shape = RoundedCornerShape(6.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(3.dp)
                        ) {
                            Text(
                                text = "Travel",
                                fontSize = 12.sp,
                                modifier = Modifier.padding(12.dp, 7.dp)
                            )
                        }
                        Card(
                            shape = RoundedCornerShape(6.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(3.dp),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Text(
                                text = "Adventure",
                                fontSize = 12.sp,
                                modifier = Modifier.padding(12.dp, 7.dp)
                            )
                        }
                        Card(
                            shape = RoundedCornerShape(6.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(3.dp),
                        ) {
                            Text(
                                text = "Discovery",
                                fontSize = 12.sp,
                                modifier = Modifier.padding(12.dp, 7.dp)
                            )
                        }
                    }
                }

                Text(
                    text = post.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(9.dp, 6.dp)
                )

                Row(
                    modifier = Modifier.padding(9.dp, 6.dp)
                ) {

                    Text(
                        text = "Categories : ",
                        fontWeight = FontWeight.SemiBold,
                        fontStyle = FontStyle.Normal,
                        fontSize = 11.sp
                    )

                    Text(
                        text = "Explorer, Nature, Safari, Travelling",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, bottom = 5.dp)
                ) {
                    // Image and text at start
                    Icon(
//                            imageVector = Icons.Filled.Person,
                        painterResource(id = R.drawable.ic_calendar),
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(25.dp)
                            .padding(9.dp, 0.dp, 0.dp, 0.dp),
                        contentDescription = "New Album"
                    )

                    Text(
                        text = formatDate(post.date),
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(3.dp, 4.dp)
                    )
                }


                Text(
                    text = plainText(post.excerpt),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(9.dp, 1.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp)
                ) {
                    // Image and text at start
                    Icon(
//                            imageVector = Icons.Filled.Person,
                        painterResource(id = R.drawable.ic_person),
                        modifier = Modifier
                            .size(30.dp)
                            .padding(9.dp, 0.dp, 0.dp, 0.dp),
                        contentDescription = "New Album"
                    )

                    Text(
                        "hesta-admin",
                        modifier = Modifier
                            .align(alignment = Alignment.CenterVertically)
                            .padding(9.dp, 0.dp, 0.dp, 0.dp), fontSize = 12.sp
                    )
                    // Spacer(modifier = Modifier.weight(1f))

                    Spacer(modifier = Modifier.weight(1f))

                    Image(
                        painter = painterResource(id = R.drawable.ic_share),
                        contentDescription = "Image 2",
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 4.dp)
                    )
                    Button(

                        onClick = { /* Handle button click */
                            navController.navigate(Screens.Detail.route +"/${post.title}"+ "/${formatDate(post.date)}" + "/${plainText(post.content)}" + "/${post.imageUrl?.let {
                                getFilenameFromUrl(
                                    it
                                )
                            }}")
                        },
                        modifier = Modifier.padding(end = 9.dp)
                    ) {
                        Text(text = "Read More")
                    }
                }
            }
        }
    }
}


@Composable
fun showTopLayout(post: Post) {

    //top cards
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Box(modifier = Modifier.height(230.dp)) {
            Image(
//                painter = painterResource(id = R.drawable.sample_pic),
                painter = rememberAsyncImagePainter(post.imageUrl),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Content on top of the card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopStart)
                    .padding(9.dp, 8.dp)
            ) {
                Text(
                    text = "Categories : Travelling, Safari, Nature...",
                    fontSize = 13.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }

            // Content at the bottom of the card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(9.dp)
            ) {
                Text(
                    text = post.title,
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 2.dp)
                ) {
                    // Image and text at start
                    Icon(
//                            imageVector = Icons.Filled.Person,
                        painterResource(id = R.drawable.ic_calendar),
                        tint = Color.White,
                        modifier = Modifier
                            .size(22.dp),
                        contentDescription = "New Album"
                    )

                    Text(
                        text = formatDate(post.date),
                        fontSize = 13.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    // Image and text at start
                    Icon(
//                            imageVector = Icons.Filled.Person,
                        painterResource(id = R.drawable.ic_person),
                        tint = Color.White,
                        modifier = Modifier
                            .size(22.dp),
                        contentDescription = "New Album"
                    )



                    Text(
                        "Hesta-Admin",
                        modifier = Modifier
                            .padding(start = 7.dp)
                            .align(alignment = Alignment.CenterVertically), fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    // Spacer(modifier = Modifier.weight(1f))

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        painter = painterResource(id = R.drawable.ic_share),
                        tint = Color.White,
                        contentDescription = "Image 2",
                        modifier = Modifier.size(24.dp),
                    )

                }
            }
        }
    }
}







//@Composable
//private fun DrawInnerShadow() {
//    Canvas(modifier = Modifier.fillMaxSize()) {
//        drawIntoCanvas { canvas ->
//            val shadowSize = 16f
//            val shadowColor = Color.Black.copy(alpha = 0.2f)
//
//            val width = size.width
//            val height = size.height
//
//            canvas.nativeCanvas.apply {
//                drawRect(0f, 0f, width, shadowSize, Color.Black.copy(alpha = 0.2f)) // Top shadow
//                drawRect(0f, height - shadowSize, width, height, shadowColor) // Bottom shadow
//            }
//        }
//    }
//}



fun formatDate(dateString: String): String {
    val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.ENGLISH)
    val dateTime = LocalDateTime.parse(dateString)
    return dateTime.format(formatter)
}


fun plainText(text: String): String {
    return HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
}



//to get only image name
fun getFilenameFromUrl(url: String): String {
    // Split the URL by "/"
    val parts = url.split("/")

    // Get the last part of the URL, which should be the filename
    val filename = parts.last()

    return filename
}

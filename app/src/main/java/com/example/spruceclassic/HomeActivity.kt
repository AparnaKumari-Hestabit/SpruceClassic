package com.example.spruceclassic

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.spruceclassic.Utility.ApiService
import com.example.spruceclassic.navigation.NavGraph
import com.example.spruceclassic.navigation.Screens
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    var selectedItem by remember { mutableStateOf("Home") }
    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var showCategoriesList by remember { mutableStateOf(false) }

    //to get category from api (Category API)
    var categories by remember { mutableStateOf<List<Categories>?>(null) }
    var subItems by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(Unit) {
       callAPI { fetchedPosts ->
            categories = fetchedPosts
           // Extracting only slugs from the list of Categories and converting them to List<String>
           val slugsList: List<String> = fetchedPosts.map { it.slug }
           subItems = slugsList
        }
    }

    val scope = rememberCoroutineScope()

    val navController = rememberNavController()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                NavigationDrawerContent(
                    currentSelectedItem = selectedItem,
                    showCategoriesList = showCategoriesList,
                    subItems = subItems,
                    onItemSelected = { item ->
                        selectedItem = item
                        if (item == "Categories") {
                            showCategoriesList = !showCategoriesList

                        } else if (item == "Post"){
                            navController.navigate(Screens.Post.route)
                            scope.launch {
                                drawerState.close()
                            }
                        } else if (item == "Home"){
                            navController.navigate(Screens.Home.route)
                            scope.launch {
                                drawerState.close()
                            }
                        }
                        else {


                            val selectedCategory = categories?.find { it.slug == item }
                            selectedCategory?.let {

                                navController.navigate(Screens.Category.route+"/${item}" +"/${it.id.toString()}")
                                scope.launch {
                                    drawerState.close()
                                }

                                // Do whatever you want with the selected category's ID
                                Log.d("Selected Category ID", it.id.toString())
                            }



                        }

                        Log.e("TAG ", " selected item : " + selectedItem )



//                        when (item) {
//                            "Post" -> navController.navigate(Screens.Post.route)
//                            "Home" -> navController.navigate(Screens.Home.route)
//                            // Add navigation for the new menu items
//                            "Explorer" -> navController.navigate(Screens.Post.route)
//                            "Nature" -> navController.navigate(Screens.Home.route)
//                            "Ocean" -> navController.navigate(Screens.Home.route)
//                            "Summer" -> navController.navigate(Screens.Home.route)
//                            "Safari" -> navController.navigate(Screens.Home.route)
//                        }
                    }
                )
            }
        },
        content = {
            // Main content of the app
            Scaffold(topBar = {
                CardWithToolbar(
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    title = "Spruce",
                    searchClick = { /* Handle search click */ }
                )
            }) {
                Box(modifier = Modifier.padding(paddingValues = it)) {
                    NavGraph(navController = navController)
                }
            }
        }
    )
}


@Composable
fun CardWithToolbar(
    onMenuClick: () -> Unit,
    title: String,
    searchClick: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { onMenuClick() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_drawer),
                    tint = Color.Black,
                    contentDescription = "Menu",
                    modifier = Modifier.size(24.dp, 20.dp)
                )
            }
            Text(
                text = title,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 27.sp,
                color = Color.Black
            )
            IconButton(onClick = searchClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Search",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp, 20.dp)
                )
            }
        }
    }
}


@Composable
fun NavigationDrawerContent(
    currentSelectedItem: String,
    showCategoriesList: Boolean,
    subItems: List<String>,
    onItemSelected: (String) -> Unit
) {
    val mainItems = listOf("Home", "Post", "Categories")

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(mainItems.size) { index ->
            val itemText = mainItems[index]
            DrawerItem(
                text = itemText,
                isSelected = currentSelectedItem == itemText,
                onClick = {
                    onItemSelected(itemText)
                },
                showArrow = itemText == "Categories" && subItems.isNotEmpty()
            )
            Divider(color = Color.Black, thickness = 0.56.dp)

            if (itemText == "Categories" && showCategoriesList && subItems.isNotEmpty()) {
                subItems.forEach { subItem ->
                    DrawerItem(
                        text = subItem,
                        isSelected = currentSelectedItem == subItem,
                        onClick = {
                            onItemSelected(subItem)
                        }
                    )
//                    Divider(color = Color.Black, thickness = 0.56.dp)
                }
            }
        }
    }
}

@Composable
fun DrawerItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    showArrow: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.Gray else Color.Black
        )
        if (showArrow) {
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_drop_down),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}



private fun callAPI(onSuccess: (List<Categories>) -> Unit) {
    val apiService = ApiService.create()
    val call: Call<List<CategoryResponse>> = apiService.getCategory()

    call.enqueue(object : Callback<List<CategoryResponse>> {
        override fun onResponse(call: Call<List<CategoryResponse>>, response: Response<List<CategoryResponse>>) {
            if (response.isSuccessful) {
                val postsResponse: List<CategoryResponse>? = response.body()
                Log.e("TAG", "Response successful: ${response.body()}")

                // Check if response is not null
                postsResponse?.let { categoriesResponse ->
                    // Map CategoryResponse objects to Categories objects
                    val categoriesList = categoriesResponse.map { categoryResponse ->
                        Categories(categoryResponse.id, categoryResponse.slug)
                    }
                    // Invoke onSuccess callback with the list of Categories
                    onSuccess(categoriesList)
                }
            }else {
                Log.e("TAG", "Response unsuccessful: ${response.errorBody()?.string()}")
            }
        }

        override fun onFailure(call: Call<List<CategoryResponse>>, t: Throwable) {
            Log.e("TAG", "API call failed: $t")
        }
    })
}


data class Categories(
    val id:Int,
    val slug:String
)

//Model class
data class CategoryResponse(
    val id:Int,
    val slug:String
)

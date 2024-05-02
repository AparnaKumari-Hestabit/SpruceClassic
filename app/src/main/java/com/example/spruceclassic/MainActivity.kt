package com.example.spruceclassic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

//            //doing this to show toolbar with the help of cardview
//            Card(
//                shape = RoundedCornerShape(8.dp),
//                colors = CardDefaults.cardColors(containerColor = Color.White)
//            ) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ){
//                    Image(
//                        painter = painterResource(id = R.drawable.ic_drawer),
//                        contentDescription = null, // Add proper content description
//                        modifier = Modifier.size(21.dp) // Adjust size as needed
//                    )
//                    // Add your content here if any
//                    Text(
//                        text = "Spruce",
//                        fontWeight = FontWeight.ExtraBold,
//                        fontSize = 25.sp,
//                        color = Color.Black
//                    )
//
//                    Image(
//                        painter = painterResource(id = R.drawable.ic_search),
//                        contentDescription = null, // Add proper content description
//                        modifier = Modifier.size(21.dp) // Adjust size as needed
//                    )
//                }
//            }


            //card design for top card-view

            val image = painterResource(id = R.drawable.sample_pic)


            Card(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(200.dp) // Set the height of the box to 200dp
                        .clip(shape = RoundedCornerShape(10.dp))
                ) {
                    Image(
                        painter = image,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Text(
                        text = "Categories: Explorer",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(6.dp)
                            .align(Alignment.TopStart)
                    )
                    // First Text at the bottom
                    Text(
                        text = "15 Travel Hacks You Gotta Know ",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.BottomStart)
                    )
                    // Second Text at the bottom
                    Text(
                        text = "Second Text",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.BottomEnd)
                    )
                }
            }


        }
    }
}
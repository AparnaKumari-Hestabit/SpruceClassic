package com.example.spruceclassic.navigation

import androidx.navigation.NavController

sealed class Screens(val route: String) {
    object Home: Screens("home_screen")
    object Post: Screens("post_screen")
    object Detail: Screens("detail_screen")
    object Category: Screens("category_screen")
}
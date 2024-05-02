package com.example.spruceclassic.navigation

import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.spruceclassic.subscreens.CategoryScreen
import com.example.spruceclassic.subscreens.DetailScreen
import com.example.spruceclassic.subscreens.HomeScreen
import com.example.spruceclassic.subscreens.PostScreen


@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.Home.route
    )
    {
        composable(route = Screens.Home.route) {
            HomeScreen(navController)
        }
        composable(route = Screens.Post.route) {
            PostScreen()
        }
        composable(route = Screens.Detail.route+ "/{id}"+"/{date}" + "/{a}" + "/{img}") { navBackStack ->

            // Extracting the argument
            val a = navBackStack.arguments?.getString("id")
            val b = navBackStack.arguments?.getString("date")
            val c = navBackStack.arguments?.getString("a")
            val img = navBackStack.arguments?.getString("img")

            DetailScreen(a, b, c, img)
        }
        composable(route = Screens.Category.route + "/{cat}" + "/{id}"){ navBackStack ->

            val a = navBackStack.arguments?.getString("cat")
            val b = navBackStack.arguments?.getString("id")
            CategoryScreen(a, b, navController)
        }
    }
}
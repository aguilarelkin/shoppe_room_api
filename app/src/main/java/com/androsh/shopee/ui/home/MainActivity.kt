package com.androsh.shopee.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.androsh.shopee.ui.description.DescriptionScreen
import com.androsh.shopee.ui.info.Info
import com.androsh.shopee.ui.navigation.Route
import com.androsh.shopee.ui.operation.Operation
import com.androsh.shopee.ui.theme.ShopeeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopeeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainNavigation(innerPadding)
                }
            }
        }
    }
}

@Composable
fun MainNavigation(innerPadding: PaddingValues) {
    val navController = rememberNavController()
    NavigationHost(navController = navController, innerPadding)
}

@Composable
fun NavigationHost(navController: NavHostController, innerPadding: PaddingValues) {

    NavHost(navController = navController, startDestination = Route.Home.route) {
        composable(Route.Home.route) {
            Info(navController, innerPadding)
        }
        composable(route = Route.Description.route, arguments = listOf(navArgument("id") {
            type = NavType.StringType
        })) {
            val id = it.arguments?.getString("id")
            DescriptionScreen(navController, id)
        }
        composable(route = Route.Operation.route, arguments = listOf(navArgument("id") {
            type = NavType.StringType
        })) {
            val id = it.arguments?.getString("id")
            Operation(id)
        }
    }
}
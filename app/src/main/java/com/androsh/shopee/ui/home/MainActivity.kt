package com.androsh.shopee.ui.home

import android.app.ActivityManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.androsh.shopee.R
import com.androsh.shopee.ui.description.DescriptionScreen
import com.androsh.shopee.ui.description.DescriptionViewModel
import com.androsh.shopee.ui.info.Info
import com.androsh.shopee.ui.info.InfoViewModel
import com.androsh.shopee.ui.info.offline.InfoOffline
import com.androsh.shopee.ui.info.offline.InfoViewModelOffline
import com.androsh.shopee.ui.login.LoginGoogle
import com.androsh.shopee.ui.login.LoginViewModel
import com.androsh.shopee.ui.navigation.Route
import com.androsh.shopee.ui.operation.Operation
import com.androsh.shopee.ui.operation.OperationViewModel
import com.androsh.shopee.ui.operation.offline.OperationOffline
import com.androsh.shopee.ui.operation.offline.OperationOfflineViewModel
import com.androsh.shopee.ui.theme.ShopeeTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Only use TaskDescription.Builder if the API level is 28 (Pie) or higher
            val taskDescription = ActivityManager.TaskDescription.Builder()
                .setLabel(getString(R.string.app_name)) // Use app name from resources
                .setPrimaryColor(getColor(R.color.black)) // Replace with your color
                .build()
            setTaskDescription(taskDescription)
        }
        enableEdgeToEdge()
        setContent {
            ShopeeTheme(darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                        MainNavigation(innerPadding)
                    }
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
    val infoViewModelOffline = hiltViewModel<InfoViewModelOffline>()
    val infoViewModel = hiltViewModel<InfoViewModel>()

    val descriptionViewModel = hiltViewModel<DescriptionViewModel>()

    val operationViewModel: OperationViewModel = hiltViewModel<OperationViewModel>()
    val operationOfflineViewModel: OperationOfflineViewModel =
        hiltViewModel<OperationOfflineViewModel>()
    val loginViewModel: LoginViewModel = hiltViewModel<LoginViewModel>()

    val uiState by loginViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        loginViewModel.isLoginGoogle()
    }
    val startDestination =
        Route.Home.route//if (uiState.isLogin) Route.Home.route else Route.Login.route

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Route.Home.route) {
            Info(navController, innerPadding, infoViewModel, loginViewModel)
        }
        composable(Route.HomeOffline.route) {
            InfoOffline(
                navController = navController,
                innerPadding = innerPadding,
                infoViewModelOffline = infoViewModelOffline,
                loginViewModel
            )
        }

        composable(route = Route.Description.route, arguments = listOf(navArgument("id") {
            type = NavType.StringType
        })) {
            DescriptionScreen(navController, getArgument(it, "id"), false, descriptionViewModel)
        }
        composable(
            route = Route.DescriptionOffline.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {
            DescriptionScreen(
                navController,
                getArgument(it, "id")/*?.replace("{","")?.replace("}","")*/,
                true,
                descriptionViewModel
            )
        }

        composable(Route.OperationCreate.route) {
            Operation(
                operationViewModel, infoViewModel = infoViewModel, navController = navController
            )
        }
        composable(route = Route.Operation.route, arguments = listOf(navArgument("id") {
            type = NavType.StringType
        })) {
            Operation(
                operationViewModel, id = getArgument(it, "id"), infoViewModel, navController
            )
        }

        composable(Route.OperationOfflineCreate.route) {
            OperationOffline(
                operationOfflineViewModel,
                navController = navController,
                infoViewModelOffline = infoViewModelOffline
            )
        }
        composable(route = Route.OperationOffline.route, arguments = listOf(navArgument("id") {
            type = NavType.StringType
        })) {
            OperationOffline(
                operationOfflineViewModel,
                id = getArgument(it, "id"),
                navController,
                infoViewModelOffline
            )
        }
        composable(route = Route.Login.route) {
            LoginGoogle(loginViewModel, navController)
        }
    }
}

private fun getArgument(navBackStackEntry: NavBackStackEntry, key: String): String? {
    return navBackStackEntry.arguments?.getString(key)
}
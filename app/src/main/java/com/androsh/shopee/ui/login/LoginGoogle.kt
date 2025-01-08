package com.androsh.shopee.ui.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.androsh.shopee.ui.navigation.Route
import com.androsh.shopee.ui.theme.DarkColor

@Composable
fun LoginGoogle(loginViewModel: LoginViewModel, navController: NavHostController) {
    val uiState by loginViewModel.uiState.collectAsState()

    Scaffold(containerColor = DarkColor.primaryContainer) { paddingValues: PaddingValues ->
        val pa = paddingValues
        val context: Context = LocalContext.current
        if (uiState.isLoading) {
            CircularProgressIndicator(
                color = Color.White, // Color del progreso
                strokeWidth = 8.dp, // Grosor del indicador
                modifier = Modifier.size(96.dp) // Tamaño del ProgressBar
            )
        }

        if (uiState.isSuccess) {
            LaunchedEffect(uiState.isSuccess) {
                Toast.makeText(context, "Login Exitoso", Toast.LENGTH_LONG).show()
                navController.navigate(Route.Home.route)
            }
        }

        // Si hay un error, mostramos el mensaje de error
        if (!uiState.errorMessage.isNullOrBlank()) {
            LaunchedEffect(uiState.errorMessage) {
                Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_LONG).show()
            }
        }
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    color = DarkColor.onPrimary,
                    text = "BIENVENIDO",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = {
                        loginViewModel.loginWithGoogle()

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkColor.tertiary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(8.dp),
                ) {
                    Text(
                        text = "Google", style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,

                            ), modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

        }
    }

}
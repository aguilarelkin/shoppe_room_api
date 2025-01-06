package com.androsh.shopee.ui.description

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.androsh.shopee.R
import com.androsh.shopee.ui.theme.DarkColor

@Composable
fun DescriptionScreen(
    navController: NavHostController,
    id: String?,
    offline: Boolean = false,
    descriptionViewModel: DescriptionViewModel
) {
    if (id == null) {
        navController.popBackStack()
        return
    }
    if (offline) {
        descriptionViewModel.getProductRoomId(id)
    } else {
        descriptionViewModel.getProductId(id)
    }
    DescriptionCard(descriptionViewModel, navController)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DescriptionCard(
    descriptionViewModel: DescriptionViewModel, navController: NavHostController
) {
    val stateProduct by descriptionViewModel.stateProduct.collectAsState()
    val stateLoading by descriptionViewModel.stateLoading.collectAsState()
    Scaffold(topBar = {
        TopAppBar(title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "",
                    color = DarkColor.onPrimary,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }, modifier = Modifier.height(70.dp), colors = TopAppBarDefaults.topAppBarColors(
            containerColor = DarkColor.primary,
            titleContentColor = DarkColor.onPrimary,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White
        ), navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        })
    }) { paddingValues ->
        val paddingValues = paddingValues
        Box(modifier = Modifier.fillMaxSize()) {
            Card(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable { },
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                if (stateLoading) {
                    Text(text = "Loading")
                } else {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Text(
                                text = stateProduct.title, modifier = Modifier.padding(16.dp)
                            )
                            Box(modifier = Modifier.fillMaxWidth()) {
                                AsyncImage(
                                    model = ImageRequest.Builder(context = LocalContext.current)
                                        .data(stateProduct.images.firstOrNull()).crossfade(true)
                                        .placeholder(R.drawable.app).build(),
                                    contentDescription = "image",

                                    contentScale = ContentScale.FillHeight,
                                    modifier = Modifier
                                        .align(
                                            Alignment.Center
                                        )
                                        .padding(vertical = 8.dp)
                                        .height(100.dp)
                                        .fillMaxWidth(),
                                )

                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Text(
                                    text = "$ ${stateProduct.price}",
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .padding(horizontal = 16.dp)
                                )
                                Text(
                                    text = "⭐\uFE0F ${stateProduct.rating}",
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .padding(horizontal = 16.dp)
                                )

                            }
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "\uD83D\uDCE6 ${stateProduct.stock}",
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .padding(horizontal = 16.dp)
                                )
                            }
                            Text(
                                text = stateProduct.description,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .fillMaxWidth(),
                                color = MaterialTheme.colorScheme.onBackground,
                                textAlign = TextAlign.Center,
                                lineHeight = 24.sp,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }


}

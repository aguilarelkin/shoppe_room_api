package com.androsh.shopee.ui.description

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.androsh.shopee.R

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
    DescriptionCard(descriptionViewModel)
}


@Composable
private fun DescriptionCard(descriptionViewModel: DescriptionViewModel) {
    val stateProduct by descriptionViewModel.stateProduct.collectAsState()
    val stateLoading by descriptionViewModel.stateLoading.collectAsState()

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
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Text(
                            text = stateProduct.title, modifier = Modifier
                                .padding(16.dp)
                        )
                        Box(modifier = Modifier.fillMaxWidth()) {
                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data(stateProduct.images.first())
                                    .crossfade(true)
                                    .placeholder(R.drawable.app)
                                    .build(),
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
                                modifier = Modifier.wrapContentSize()
                            )
                            Text(
                                text = " ${stateProduct.rating}",
                                modifier = Modifier.wrapContentSize()
                            )

                        }
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Text(text = "${stateProduct.stock}")
                        }
                        Text(text = stateProduct.description)
                    }
                }
            }
        }
    }

}

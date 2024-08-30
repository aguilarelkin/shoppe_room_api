package com.androsh.shopee.ui.description

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun DescriptionScreen(navController: NavHostController, id: String?) {
    if (id == null) {
        navController.popBackStack()
    }
    DescriptionCard()
}

@Preview(showSystemUi = true)
@Composable
private fun DescriptionCard() {
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
            LazyColumn(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                item {
                    Text(
                        text = "nombre", modifier = Modifier
                            .padding(16.dp)
                    )
                    Image(imageVector = Icons.Filled.Info, contentDescription = "des")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "precio", modifier = Modifier.wrapContentSize())
                        Text(text = "descuento", modifier = Modifier.wrapContentSize())
                    }
                    Box(modifier = Modifier.fillMaxWidth()){
                        Text(text = "Stock")
                    }
                    Text(text = "description")
                }
            }
        }
    }

}

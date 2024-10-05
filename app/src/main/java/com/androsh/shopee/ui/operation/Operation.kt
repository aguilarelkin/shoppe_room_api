package com.androsh.shopee.ui.operation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun Operation(
    operationViewModel: OperationViewModel,
    id: String? = null,
) {
    Log.i("aaaaaaaaaaaaaaaadfa65", id.toString())
    DataOperation(id)
}

@Composable
private fun DataOperation(id: String?) {
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf("") }

    var discount by rememberSaveable { mutableStateOf("") }
    var rating by rememberSaveable { mutableStateOf("") }
    var stock by rememberSaveable { mutableStateOf("") }
    var brand by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("") }
    var thumbnail by rememberSaveable { mutableStateOf("https://cdn.dummyjson.com/products/images/beauty/Essence%20Mascara%20Lash%20Princess/thumbnail.png") }
    var image by rememberSaveable { mutableStateOf("https://cdn.dummyjson.com/products/images/beauty/Essence%20Mascara%20Lash%20Princess/1.png") }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, vertical = 40.dp)
        ) {
            item {
                LevelText(data = "Title")
                FieldName(dataValue = title) {
                    title = it
                }
                LevelText(data = "Description")
                FieldName(dataValue = description) {
                    description = it
                }
                LevelText(data = "Price")
                FieldName(dataValue = price) {
                    price = it
                }

                LevelText(data = "Discount")
                FieldName(dataValue = discount) {
                    discount = it
                }
                LevelText(data = "Rating")
                FieldName(dataValue = rating) {
                    rating = it
                }
                LevelText(data = "Stock")
                FieldName(dataValue = stock) {
                    stock = it
                }
                LevelText(data = "Brand")
                FieldName(dataValue = brand) {
                    brand = it
                }
                LevelText(data = "Category")
                FieldName(dataValue = category) {
                    category = it
                }

                Button(onClick = {
                    dataOperation(
                        title,
                        description,
                        price,
                        discount,
                        rating,
                        stock,
                        brand,
                        category,
                        thumbnail,
                        image
                    )
                }) {
                    val a = if (id == null) {
                        "create"
                    } else {
                        "update"
                    }

                    Text(text = a)
                }
            }
        }
    }
}

@Composable
private fun LevelText(data: String) {
    Text(
        text = data,
        modifier = Modifier
            .wrapContentSize()
    )

}

@Composable
private fun FieldName(dataValue: String, dataOnChange: (String) -> Unit) {
    TextField(
        value = dataValue,
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        onValueChange = { dataOnChange(it) },

        )
}

private fun dataOperation(
    title: String,
    description: String,
    price: String,
    discount: String,
    rating: String,
    stock: String,
    brand: String,
    category: String,
    thumbnail: String,
    image: String
) {
}
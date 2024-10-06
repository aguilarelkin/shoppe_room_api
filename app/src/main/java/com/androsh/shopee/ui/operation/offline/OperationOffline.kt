package com.androsh.shopee.ui.operation.offline

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.androsh.shopee.domain.models.ProductModel
import com.androsh.shopee.ui.info.offline.InfoViewModelOffline

@Composable
fun OperationOffline(
    operationViewModel: OperationOfflineViewModel,
    id: String? = null,
    navController: NavHostController,
    infoViewModelOffline: InfoViewModelOffline
) {
    DataOperation(id, operationViewModel, navController, infoViewModelOffline)
}

@Composable
private fun DataOperation(
    id: String?,
    operationViewModel: OperationOfflineViewModel,
    navController: NavHostController,
    infoViewModelOffline: InfoViewModelOffline
) {
    val uiState by operationViewModel.uiState.collectAsState()
    if (uiState.isOperationSuccessResult) {
        operationViewModel.initUiState()
        infoViewModelOffline.onProductCreated()
        successFull(navController)
    }
    data class ProductModels(
        var id: Int = 0,
        var title: String = "",
        var description: String = "",
        var price: Double = 0.0,
        var discountPercentage: Double = 0.0,
        var rating: Double = 0.0,
        var stock: Int = 0,
        var brand: String? = "",
        var category: String = "",
        var thumbnail: String = "",
        var images: List<String> = emptyList()
    )

    val productSaver = Saver<ProductModel, Map<String, Any?>>(

        save = { product ->
            mapOf(
                "id" to product.id,
                "title" to product.title,
                "description" to product.description,
                "price" to product.price,
                "discountPercentage" to product.discountPercentage,
                "rating" to product.rating,
                "stock" to product.stock,
                "brand" to product.brand,
                "category" to product.category,
                "thumbnail" to product.thumbnail,
                "images" to product.images
            )
        },
        restore = { map ->
            ProductModel(
                id = map["id"] as Int,
                title = map["title"] as String,
                description = map["description"] as String,
                price = map["price"] as Double,
                discountPercentage = map["discountPercentage"] as Double,
                rating = map["rating"] as Double,
                stock = map["stock"] as Int,
                brand = map["brand"] as String,
                category = map["category"] as String,
                thumbnail = map["thumbnail"] as String,
                images = map["images"] as List<String>
            )
        }
    )
    val productData = rememberSaveable(stateSaver = productSaver) {
        mutableStateOf(
            ProductModel(
                thumbnail = "https://cdn.dummyjson.com/products/images/beauty/Essence%20Mascara%20Lash%20Princess/thumbnail.png",
                images = listOf("https://cdn.dummyjson.com/products/images/beauty/Essence%20Mascara%20Lash%20Princess/1.png")
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, vertical = 40.dp)
        ) {
            item {
                val context = LocalContext.current
                LevelText(data = "Title")
                FieldName(dataValue = productData.value.title) {
                    productData.value = productData.value.copy(title = it)
                }
                LevelText(data = "Description")
                FieldName(dataValue = productData.value.description) {
                    productData.value = productData.value.copy(description = it)
                }
                LevelText(data = "Price")
                FieldName(dataValue = productData.value.price.toString()) {
                    productData.value = productData.value.copy(price = validDouble(it))
                }

                LevelText(data = "Discount")
                FieldName(dataValue = productData.value.discountPercentage.toString()) {
                    productData.value = productData.value.copy(discountPercentage = validDouble(it))
                }
                LevelText(data = "Rating")
                FieldName(dataValue = productData.value.rating.toString()) {
                    productData.value = productData.value.copy(rating = validDouble(it))
                }
                LevelText(data = "Stock")
                FieldName(dataValue = productData.value.stock.toString()) {
                    productData.value = productData.value.copy(stock = validInt(it))
                }
                LevelText(data = "Brand")
                productData.value.brand?.let {
                    FieldName(dataValue = it) { data ->
                        productData.value = productData.value.copy(brand = data)
                    }
                }
                LevelText(data = "Category")
                FieldName(dataValue = productData.value.category) {
                    productData.value = productData.value.copy(category = it)
                }
                if (uiState.error != null) {
                    Snackbar {
                        Text(text = uiState.error.toString())
                    }
                }
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 4.dp
                    )
                } else {
                    Button(onClick = {
                        val isValid =
                            productData.value.let { product -> product.title.isNotBlank() && product.description.isNotBlank() && product.price >= 0.0 && product.discountPercentage >= 0.0 && product.rating >= 0.0 && product.stock >= 0 && product.category.isNotBlank() }
                        if (isValid) {
                            operation(id, productData.value, operationViewModel)
                        } else {
                            Toast.makeText(
                                context,
                                "Please fill in all required fields",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }) {
                        Text(text = if (id == null) "Create" else "Update")
                    }
                }

                /*                LaunchedEffect(uiState.isOperationSuccessResult) {
                                    if (uiState.isOperationSuccessResult) {
                                        navController.popBackStack()
                                    }
                                }*/

            }
        }
    }
}

private fun successFull(navController: NavHostController) {
    navController.popBackStack()
}

@Composable
private fun LevelText(data: String) {
    Text(
        text = data, modifier = Modifier.wrapContentSize()
    )

}

@Composable
private fun FieldName(dataValue: String, dataOnChange: (String) -> Unit) {
    /*    val onValueChangeRemembered = remember {
            { text: String ->
                val newValue = when (T::class) {
                    String::class -> text
                    Int::class -> text.toIntOrNull() ?: dataValue
                    Double::class -> text.toDoubleOrNull() ?: dataValue
                    // ... otros tipos
                    else -> throw IllegalArgumentException("Unsupported type")
                }
                dataOnChange(newValue as T)
            }
        }*/
    TextField(
        value = dataValue,
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        onValueChange = { dataOnChange(it) },
    )
}

private fun operation(
    id: String?,
    product: ProductModel,
    operationViewModel: OperationOfflineViewModel,
) {
    if (id == null) {
        operationViewModel.insertProduct(product)
    } else {
        operationViewModel.updateProductDb(product)
    }
}

private fun validDouble(cost: String): Double {
    return try {
        cost.toDouble()
    } catch (e: Exception) {
        0.0
    }
}

private fun validInt(edId: String): Int {
    return try {
        Integer.parseInt(edId)
    } catch (e: Exception) {
        0
    }
}
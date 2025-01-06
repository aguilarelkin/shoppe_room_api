package com.androsh.shopee.ui.operation.offline

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.androsh.shopee.domain.models.ProductModel
import com.androsh.shopee.ui.info.offline.InfoViewModelOffline
import com.androsh.shopee.ui.operation.OperationUiState
import com.androsh.shopee.ui.operation.ProductUtil.productSaver
import com.androsh.shopee.ui.theme.DarkColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperationOffline(
    operationViewModel: OperationOfflineViewModel,
    id: String? = null,
    navController: NavHostController,
    infoViewModelOffline: InfoViewModelOffline
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "DummyJSON",
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
        },
    ) { paddingValues ->

        DataOperation(id, operationViewModel, navController, infoViewModelOffline, paddingValues)

    }
}

@Composable
private fun DataOperation(
    id: String?,
    operationViewModel: OperationOfflineViewModel,
    navController: NavHostController,
    infoViewModelOffline: InfoViewModelOffline,
    paddingValues: PaddingValues
) {
    val uiState by operationViewModel.uiState.collectAsState()
    val product by operationViewModel.producState.collectAsState()
    val productData = rememberSaveable(stateSaver = productSaver) {
        mutableStateOf(
            ProductModel(
                thumbnail = "https://cdn.dummyjson.com/products/images/beauty/Essence%20Mascara%20Lash%20Princess/thumbnail.png",
                images = listOf("https://cdn.dummyjson.com/products/images/beauty/Essence%20Mascara%20Lash%20Princess/1.png")
            )
        )
    }
    if (!id.isNullOrBlank()) {
        LaunchedEffect(key1 = id) {
            operationViewModel.searchProduct(id)
        }
        LaunchedEffect(key1 = product) {
            productData.value = product
        }
    }
    if (uiState.isOperationSuccessResult) {
        operationViewModel.initUiState()
        infoViewModelOffline.onProductCreated()
        SuccessFull(navController, uiState.create)
    }
    ProductFormContent(productData, uiState, operationViewModel, LocalContext.current, id)
}


@Composable
private fun ProductFormContent(
    productData: MutableState<ProductModel>,
    uiState: OperationUiState,
    operationViewModel: OperationOfflineViewModel,
    current: Context,
    id: String?
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, vertical = 40.dp)
        ) {
            item {
                val categories = listOf(
                    "Electronics",
                    "Clothing",
                    "Books",
                    "Home",
                    "Beauty",
                    "Fragrances",
                    "Groceries",
                    "Decoration",
                    "Vehicle",
                    "Other"
                )
                val context = LocalContext.current

                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LevelText(data = "Title")
                        FieldName(dataValue = productData.value.title) {
                            productData.value = productData.value.copy(title = it)
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LevelText(data = "Price")
                        FieldName(dataValue = productData.value.price.toString()) {
                            productData.value = productData.value.copy(price = validDouble(it))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                LevelText(data = "Description")
                FieldName(dataValue = productData.value.description) {
                    productData.value = productData.value.copy(description = it)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LevelText(data = "Discount")
                        FieldName(dataValue = productData.value.discountPercentage.toString()) {
                            productData.value =
                                productData.value.copy(discountPercentage = validDouble(it))
                        }

                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LevelText(data = "Rating")
                        FieldName(dataValue = productData.value.rating.toString()) {
                            productData.value = productData.value.copy(rating = validDouble(it))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                LevelText(data = "Stock")
                FieldName(dataValue = productData.value.stock.toString()) {
                    productData.value = productData.value.copy(stock = validInt(it))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LevelText(data = "Brand")
                        ListDropdown(info = "Brand",
                            categories = categories,
                            selectedCategory = productData.value.brand
                                ?: "", // Si `brand` es null, se usa un string vacío
                            onCategorySelected = { data ->
                                productData.value = productData.value.copy(brand = data)
                            })
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LevelText(data = "Category")
                        ListDropdown(info = "Category",
                            categories = categories,
                            selectedCategory = productData.value.category,
                            onCategorySelected = { newCategory ->
                                productData.value = productData.value.copy(category = newCategory)
                            })
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                if (uiState.error != null) {
                    Snackbar {
                        Text(text = uiState.error.toString())
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary, strokeWidth = 4.dp
                    )
                } else {
                    Button(
                        onClick = {
                            val isValid = productData.value.let { product ->
                                product.title.isNotBlank() && product.description.isNotBlank() && product.price >= 0.0 && product.discountPercentage >= 0.0 && product.rating >= 0.0 && product.stock >= 0 && product.category.isNotBlank()
                            }
                            if (isValid) {
                                operation(id, productData.value, operationViewModel)
                            } else {
                                Toast.makeText(
                                    context,
                                    "¡Por favor, completa todos los campos necesarios!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(8.dp),
                    ) {
                        Text(
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 20.sp, fontWeight = FontWeight.Bold
                            ),
                            text = if (id == null) "Create" else "Update",
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))/*                LaunchedEffect(uiState.isOperationSuccessResult) {
                                    if (uiState.isOperationSuccessResult) {
                                        navController.popBackStack()
                                    }
                                }*/

            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListDropdown(
    info: String,
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(selectedCategory) }
    LaunchedEffect(key1 = selectedCategory) {
        text = selectedCategory
    }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(value = text,
            onValueChange = {},
            readOnly = true,
            label = { Text(info) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            categories.forEach { category ->
                DropdownMenuItem(text = { Text(text = category) }, onClick = {
                    text = category
                    onCategorySelected(category)
                    expanded = false
                })
            }
        }
    }
}


@Composable
private fun SuccessFull(navController: NavHostController, create: Boolean) {
    val context = LocalContext.current

    LaunchedEffect(create) {
        val message = if (create) {
            "¡Producto creado exitosamente!"
        } else {
            "¡Producto actualizado exitosamente!"
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    navController.popBackStack()
}


@Composable
private fun LevelText(data: String) {
    Text(
        text = data, modifier = Modifier.wrapContentSize()
    )

}

@Composable
private fun FieldName(dataValue: String, dataOnChange: (String) -> Unit) {/*    val onValueChangeRemembered = remember {
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
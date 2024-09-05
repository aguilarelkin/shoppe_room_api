package com.androsh.shopee.ui.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.androsh.shopee.ui.navigation.Route

@Composable
fun Info(
    navController: NavHostController,
    innerPadding: PaddingValues,
    infoViewModel: InfoViewModel
) {
    Box(modifier = Modifier.padding(innerPadding)) {
        MainInfo(navController, infoViewModel)
    }
}

//@Preview(showSystemUi = true)
@Composable
private fun MainInfo(navController: NavHostController, infoViewModel: InfoViewModel) {
    Column {
        TopBar(navController = navController, onSearch = { println(it) })
        CategoryProduct()
        LevelText(product = "Productos")
        ListProduct(navController, infoViewModel)
    }
}

@Composable
private fun TopBar(
    navController: NavHostController,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

        TextField(
            value = text,
            onValueChange = {
                text = it
            },
            modifier = modifier
                .weight(1f)
                .padding(8.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            placeholder = {
                Text("Search")
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch(text) })
        )

        IconButton(
            onClick = { navController.navigate(Route.Operation.operationProduct("new")) },
            modifier = Modifier
                .padding(8.dp)
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add", tint = Color.Blue)
        }

        DropdownButton(onSelectionChange = { selectedOption ->
            // Acción a realizar al seleccionar una opción
            println("Selected option: $selectedOption")
        })
    }
}

@Composable
fun DropdownButton(
    onSelectionChange: (String) -> Unit
) {
    val options =
        listOf("Limpiar Filtro", "Precio", "Descuento", "Categoria", "Stock", "Marca", "Rating")
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedOptionText by rememberSaveable { mutableStateOf(options[0]) }

    Box(modifier = Modifier.padding(8.dp)) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Filled.List,
                contentDescription = "Dropdown arrow",
                tint = Color.Blue
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(text = { Text(selectionOption) }, onClick = {
                    selectedOptionText = selectionOption
                    expanded = false
                    onSelectionChange(selectionOption)
                })
            }
        }
    }
}

@Composable
private fun CategoryProduct() {
    val options =
        listOf("Limpiar Filtro", "Precio", "Descuento", "Categoria", "Stock", "Marca", "Rating")
    if (options.isNotEmpty()) {
        LazyRow {
            items(options) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable { },
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(Brush.horizontalGradient(listOf(Color.White, Color.Cyan)))
                            .padding(16.dp)
                    ) {
                        Text(text = it, color = Color.Black)
                    }
                }

            }
        }

    }
}

@Composable
private fun LevelText(product: String) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = product,
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center)
        )
    }
}

@Composable
private fun ListProduct(navController: NavHostController, infoViewModel: InfoViewModel) {
    val options =
        listOf("Limpiar Filtro", "Precio", "Descuento", "Categoria", "Stock", "Marca", "Rating")
    if (options.isNotEmpty()) {
        LazyVerticalGrid(columns = GridCells.Adaptive(150.dp)) {
            items(options) {
                ItemProduct(it, navController)
            }

        }
    }
}

@Composable
private fun ItemProduct(product: String, navController: NavHostController) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { startDescription(navController, product) },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                imageVector = Icons.Filled.Info,
                contentDescription = "des",
                modifier = Modifier
                    .align(
                        Alignment.Center
                    )
                    .padding(vertical = 8.dp)
            )
        }
        Text(text = product, modifier = Modifier.padding(horizontal = 16.dp))
        Text(text = "category", modifier = Modifier.padding(horizontal = 16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "precio", modifier = Modifier.wrapContentSize())
            Text(text = "descuento", modifier = Modifier.wrapContentSize())
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            IconButton(onClick = { navController.navigate(Route.Operation.operationProduct("1")) }) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit",
                    tint = Color.Blue
                )
            }
            IconButton(onClick = {
                showDialog = true
            }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = Color.Blue
                )
            }
        }
    }
    if (showDialog) {
        DialogDelete(
            showDialog = showDialog,
            onConfirm = {
                // Acción para eliminar el usuario
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
private fun DialogDelete(showDialog: Boolean, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                Button(onClick = { onConfirm() }) {
                    Text(text = "Delete")
                }
            },
            title = {
                Text(text = "Delete product")
            },
            text = {
                Text(text = "¿Estás seguro de eliminar el producto?")
            },
            dismissButton = {
                Button(onClick = { onDismiss() }) {
                    Text(text = "Cancel")
                }
            }
        )
    }
}

private fun startDescription(navController: NavHostController, product: String) {
    try {
        navController.navigate(Route.Description.createRoute(product))
    } catch (_: Exception) {
        println("errr")
    }
}
package com.androsh.shopee.ui.info.offline

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.androsh.shopee.R
import com.androsh.shopee.domain.models.ProductModel
import com.androsh.shopee.ui.navigation.Route

@Composable
fun InfoOffline(
    navController: NavHostController,
    innerPadding: PaddingValues,
    infoViewModelOffline: InfoViewModelOffline
) {
    Box(modifier = Modifier.padding(innerPadding)) {
        MainInfo(navController, infoViewModelOffline)
    }
}

//@Preview(showSystemUi = true)
@Composable
private fun MainInfo(navController: NavHostController, infoViewModel: InfoViewModelOffline) {
    Column {
        TopBar(
            infoViewModel = infoViewModel,
            navController = navController,
            /*onSearch = { println(it) }*/

        )
        PageOffline(navController)
        CategoryProduct(infoViewModel)
        LevelText(product = "Productos")
        ListProduct(navController, infoViewModel)
    }
}

@Composable
private fun TopBar(
    infoViewModel: InfoViewModelOffline,
    navController: NavHostController,
    //onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var search by rememberSaveable { mutableStateOf("") }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

        TextField(
            value = search,
            onValueChange = {
                search = it
                infoViewModel.onChangedQuery(it)
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
            trailingIcon = {
                if (search.isNotEmpty()) {
                    IconButton(onClick = {
                        infoViewModel.onChangedQuery("")
                        infoViewModel.getProducts()
                        search = ""
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close"
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                //onSearch(search)
                infoViewModel.searchProduct(search)
            })
        )

        IconButton(
            onClick = { navController.navigate(Route.OperationCreate.route) },
            modifier = Modifier
                .padding(8.dp)
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add", tint = Color.Blue)
        }

        DropdownButton(onSelectionChange = { selectedOption ->
            infoViewModel.filterProducts(selectedOption)
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
private fun CategoryProduct(infoViewModel: InfoViewModelOffline) {
    val uiState by infoViewModel.uiState.collectAsState()
    if (uiState.categories.isNotEmpty()) {
        LazyRow {
            items(uiState.categories) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable {
                            /*infoViewModel.filterProducts(it.name)*/
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(Brush.horizontalGradient(listOf(Color.White, Color.Cyan)))
                            .padding(16.dp)
                    ) {
                        Text(text = it.name, color = Color.Black)
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
private fun ListProduct(navController: NavHostController, infoViewModel: InfoViewModelOffline) {
    val uiState by infoViewModel.uiState.collectAsState()

/*
    if (uiState.query.isEmpty() && !uiState.isFiltering && !uiState.isProductDeleted) {
        infoViewModel.getProducts()
    }
*/

    if (uiState.isLoading) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 4.dp
        )
    }
    if (uiState.error != null) {
        Snackbar {
            Text(text = "Error to verifier connexion")
        }
    }
    if (uiState.products.isNotEmpty()) {
        LazyVerticalGrid(columns = GridCells.Adaptive(150.dp)) {
            items(uiState.products) {
                ItemProduct(it, navController, infoViewModel)
            }

        }
    }


}

@Composable
private fun ItemProduct(
    product: ProductModel,
    navController: NavHostController,
    infoViewModel: InfoViewModelOffline
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { startDescription(navController, product.id.toString()) },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(product.images.first())
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
        TextData(info = product.title, size = 15.sp)
        Divider()
        TextData(info = product.category, size = 18.sp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val dataPrice = if (product.stock > 0) {
                product.price
            } else {
                "Not stock"
            }

            Text(text = "$ $dataPrice", modifier = Modifier.wrapContentSize())
            Text(text = " ${product.rating}", modifier = Modifier.wrapContentSize())
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            IconButton(onClick = {
                navController.navigate(
                    Route.Operation.route.replace(
                        "{id}",
                        product.id.toString()
                    )
                )
            }) {
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
                infoViewModel.deleteProductId(product.id.toString())
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

private fun ModOnline(navController: NavHostController) {
    navController.navigate(Route.Home.route)
}

private fun ModOffline(navController: NavHostController) {

    navController.navigate(Route.HomeOffline.route)

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PageOffline(navController: NavHostController) {
    val pageState = rememberPagerState(pageCount = { 2 })
    val scope = rememberCoroutineScope()
//
//    HorizontalPager(  state = pageState) { page ->
//        when (page) {
//            0 -> ModOnline(navController)
//            1 -> ModOffline(navController = navController)
//        }
//    }


    Row(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            shape = RectangleShape,
            onClick = {
//                scope.launch {
//                    if (pageState.currentPage > 0) {
//                        pageState.animateScrollToPage(pageState.currentPage - 1)
//                    }
//                }
                ModOnline(navController)

            },
            enabled = pageState.currentPage < pageState.pageCount - 1,
            modifier = Modifier
                .weight(1f)
                .clip(RectangleShape),
        ) {
            Text("Online")
        }
        OutlinedButton(
            shape = RectangleShape,
            onClick = {
//                scope.launch {
//                    if (pageState.currentPage < pageState.pageCount - 1) {
//                        pageState.animateScrollToPage(pageState.currentPage + 1)
//                    }
//                }
                ModOffline(navController)
            },
            enabled = pageState.currentPage > 0,
            modifier = Modifier
                .weight(1f)
                .clip(RectangleShape),
        ) {
            Text("Offline")
        }
    }
}

@Composable
fun TextData(info: String, size: TextUnit) {
    Text(
        fontSize = size,
        fontStyle = FontStyle.Normal,
        modifier = Modifier.padding(2.dp),
        text = info, softWrap = false,
        overflow = TextOverflow.Ellipsis
    )
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
        navController.navigate(Route.Description.route.replace("id", product))
    } catch (_: Exception) {
        println("errr")
    }
}

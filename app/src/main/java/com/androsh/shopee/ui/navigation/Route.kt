package com.androsh.shopee.ui.navigation

sealed class Route(val route: String) {
    data object Home : Route("home")

    data object Operation : Route("operation/{id}") {
        fun operationProduct(productId: String) = "operation/$productId"
    }

    data object Description : Route("description/{id}") {
        fun createRoute(productId: String) = "description/$productId"
    }

    data object Login : Route("login")
    data object Registry : Route("registry")
}
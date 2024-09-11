package com.androsh.shopee.ui.navigation

sealed class Route(val route: String) {
    data object Home : Route("home")

    data object Operation : Route("operation/{id}")
    data object OperationCreate : Route("operation")
    data object HomeOffline : Route("homeoffline")

    data object Description : Route("description/{id}")

    data object Login : Route("login")
    data object Registry : Route("registry")
}
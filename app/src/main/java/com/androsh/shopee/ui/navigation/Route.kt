package com.androsh.shopee.ui.navigation

sealed class Route(val route: String) {
    data object Home : Route("home")
    data object HomeOffline : Route("homeoffline")

    data object Operation : Route("operation/{id}")
    data object OperationCreate : Route("operation")
    data object OperationOffline : Route("operation/{id}")
    data object OperationOfflineCreate : Route("operation")


    data object Description : Route("description/{id}")
    data object DescriptionOffline : Route("description/{id}")

    data object Login : Route("login")
    data object Registry : Route("registry")
}
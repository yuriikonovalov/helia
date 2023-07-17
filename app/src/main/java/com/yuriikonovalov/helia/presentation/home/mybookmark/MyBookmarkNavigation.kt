package com.yuriikonovalov.helia.presentation.home.mybookmark

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

private object MyBookmarkNavigation {
    const val route = "my_bookmark"
}

fun NavGraphBuilder.myBookmarkRoute(
    onNavigateClick: () -> Unit,
    onHotelClick: (hotelId: String) -> Unit
) {
    composable(route = MyBookmarkNavigation.route) {
        MyBookmarkScreen(
            onNavigateClick = onNavigateClick,
            onHotelClick = onHotelClick
        )
    }
}

fun NavHostController.navigateToMyBookmarkRoute() {
    navigate(route = MyBookmarkNavigation.route)
}
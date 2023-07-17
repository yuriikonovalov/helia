package com.yuriikonovalov.helia.presentation.home.recentlybooked

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

private object RecentlyBookedNavigation {
    const val route = "recently_booked"
}

fun NavGraphBuilder.recentlyBookedRoute(
    onNavigateClick: () -> Unit,
    onHotelClick: (hotelId: String) -> Unit
) {
    composable(route = RecentlyBookedNavigation.route) {
        RecentlyBookedScreen(
            onNavigateClick = onNavigateClick,
            onHotelClick = onHotelClick
        )
    }
}

fun NavHostController.navigateToRecentlyBookedRoute() {
    navigate(route = RecentlyBookedNavigation.route)
}
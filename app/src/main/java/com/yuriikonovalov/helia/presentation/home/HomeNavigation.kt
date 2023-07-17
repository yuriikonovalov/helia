package com.yuriikonovalov.helia.presentation.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

object HomeNavigation {
    const val route = "home"
}

fun NavGraphBuilder.homeRoute(
    onBookmarksClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onSearchClick: () -> Unit,
    onSeeAllBookedHotelsClick: () -> Unit,
    onHotelClick: (hotelId: String) -> Unit
) {
    composable(route = HomeNavigation.route) {
        HomeScreen(
            onBookmarksClick = onBookmarksClick,
            onNotificationsClick = onNotificationsClick,
            onSearchClick = onSearchClick,
            onSeeAllBookedHotelsClick = onSeeAllBookedHotelsClick,
            onPreviewHotelClick = onHotelClick
        )
    }
}

fun NavHostController.navigateToHomeRoute(navOptions: NavOptions? = null) {
    navigate(route = HomeNavigation.route, navOptions = navOptions)
}
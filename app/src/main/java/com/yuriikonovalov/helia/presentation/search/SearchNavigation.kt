package com.yuriikonovalov.helia.presentation.search

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

object SearchNavigation {
    const val route = "search"
}

fun NavGraphBuilder.searchRoute(
    onHotelClick: (hotelId: String) -> Unit
) {
    composable(route = SearchNavigation.route) {
        SearchScreen(onHotelClick = onHotelClick)
    }
}

fun NavHostController.navigateToSearchRoute(navOptions: NavOptions? = null) {
    navigate(route = SearchNavigation.route, navOptions = navOptions)
}
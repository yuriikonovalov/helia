package com.yuriikonovalov.helia.presentation.hoteldetails

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

object HotelDetailsNavigation {
    fun route(hotelId: String) = "hotel/$hotelId"
    const val ARGUMENT = "id"
}

fun NavGraphBuilder.hotelDetailsRoute(
    onNavigationClick: () -> Unit,
    onGalleryClick: (hotelId: String) -> Unit,
    onMapClick: (hotelId: String) -> Unit,
    onReviewsClick: (hotelId: String) -> Unit
) {
    composable(
        route = "hotel/{${HotelDetailsNavigation.ARGUMENT}}",
        arguments = listOf(navArgument(HotelDetailsNavigation.ARGUMENT) {
            type = NavType.StringType
        })
    ) {
        HotelDetailsScreen(
            onNavigationClick = onNavigationClick,
            onGalleryClick = onGalleryClick,
            onMapClick = onMapClick,
            onReviewsClick = onReviewsClick
        )
    }
}

fun NavHostController.navigateToHotelDetailsRoute(hotelId: String) {
    navigate(route = HotelDetailsNavigation.route(hotelId))
}
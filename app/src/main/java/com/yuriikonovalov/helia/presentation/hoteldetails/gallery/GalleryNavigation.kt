package com.yuriikonovalov.helia.presentation.hoteldetails.gallery

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

object GalleryNavigation {
    const val ARGUMENT = "id"
    fun route(hotelId: String) = "gallery/$hotelId"
}

fun NavGraphBuilder.hotelGalleryRoute(onNavigationClick: () -> Unit) {
    composable(
        route = "gallery/{${GalleryNavigation.ARGUMENT}}",
        arguments = listOf(
            navArgument(GalleryNavigation.ARGUMENT) {
                type = NavType.StringType
            }
        )
    ) {
        GalleryScreen(onNavigationClick = onNavigationClick)
    }
}

fun NavHostController.navigateToHotelGallery(hotelId: String) {
    navigate(route = GalleryNavigation.route(hotelId))
}

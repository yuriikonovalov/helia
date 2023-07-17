package com.yuriikonovalov.helia.presentation.hoteldetails.review

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

object ReviewNavigation {
    const val ARGUMENT = "id"

    fun route(id: String) = "reviews/$id"
}

fun NavGraphBuilder.hotelReviewsRoute(onNavigationClick: () -> Unit) {
    composable(route = "reviews/{${ReviewNavigation.ARGUMENT}}",
        arguments = listOf(
            navArgument(ReviewNavigation.ARGUMENT) {
                type = NavType.StringType
            }
        )
    ) {
        ReviewScreen(onNavigationClick = onNavigationClick)
    }
}

fun NavHostController.navigateToHotelReviews(hotelId: String) {
    navigate(route = ReviewNavigation.route(hotelId))
}
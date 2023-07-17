package com.yuriikonovalov.helia.presentation.welcome.carousel

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.yuriikonovalov.helia.presentation.welcome.WelcomeNavigation

object CarouselNavigation {
    const val route = "carousel"
}

fun NavGraphBuilder.carouselRoute(
    onNavigateFurther: () -> Unit
) {
    composable(route = CarouselNavigation.route) {
        CarouselScreen(onNavigateFurther = onNavigateFurther)
    }
}

fun NavHostController.navigateToCarouselRoute() {
    navigate(CarouselNavigation.route) {
        popUpTo(WelcomeNavigation.route) {
            inclusive = true
        }
    }
}
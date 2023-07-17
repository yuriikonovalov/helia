package com.yuriikonovalov.helia.presentation.auth.authentication

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.yuriikonovalov.helia.presentation.welcome.carousel.CarouselNavigation

object AuthenticationNavigation {
    const val route = "authentication"
}

fun NavGraphBuilder.authenticationRoute(
    onSignIn: () -> Unit,
    onSignUn: () -> Unit,
    onNavigateToFillProfile: () -> Unit
) {
    composable(route = AuthenticationNavigation.route) {
        AuthenticationScreen(
            onSignInWithPassword = onSignIn,
            onSignUn = onSignUn,
            onNavigateToFillProfile = onNavigateToFillProfile
        )
    }
}

fun NavHostController.navigateToAuthenticationRoute() {
    navigate(AuthenticationNavigation.route) {
        popUpTo(CarouselNavigation.route) {
            inclusive = true
        }
    }
}
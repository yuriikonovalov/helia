package com.yuriikonovalov.helia.presentation.auth.fillprofile

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

object SignUpFillProfileNavigation {
    const val route = "fill_profile"
}

fun NavGraphBuilder.fillProfileRoute(
    onNavigateClick: () -> Unit
) {
    composable(route = SignUpFillProfileNavigation.route) {
        FillProfileScreen(
            onNavigateClick = onNavigateClick
        )
    }
}

fun NavHostController.navigateToFillProfileRoute(navOptions: NavOptions? = null) {
    navigate(route = SignUpFillProfileNavigation.route, navOptions = navOptions)
}
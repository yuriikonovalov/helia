package com.yuriikonovalov.helia.presentation.profile.security

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

private object SecurityNavigation {
    const val route = "security"
}

fun NavGraphBuilder.profileSecurityRoute(
    onNavigateClick: () -> Unit,
    onChangePasswordClick: () -> Unit
) {
    composable(route = SecurityNavigation.route) {
        SecurityScreen(
            onNavigateClick = onNavigateClick,
            onChangePasswordClick = onChangePasswordClick
        )
    }
}

fun NavHostController.navigateToProfileSecurityRoute() {
    navigate(route = SecurityNavigation.route)
}
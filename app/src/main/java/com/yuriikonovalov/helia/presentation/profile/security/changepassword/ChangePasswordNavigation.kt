package com.yuriikonovalov.helia.presentation.profile.security.changepassword

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

private object ChangePasswordNavigation {
    const val route = "change_password"
}

fun NavGraphBuilder.changePasswordRoute(
    onNavigateUp: () -> Unit
) {
    composable(route = ChangePasswordNavigation.route) {
        ChangePasswordScreen(onNavigateUp = onNavigateUp)
    }
}

fun NavHostController.navigateToChangePasswordRoute() {
    navigate(route = ChangePasswordNavigation.route)
}
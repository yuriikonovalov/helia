package com.yuriikonovalov.helia.presentation.profile.edit

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

private object EditProfileNavigation {
    const val route = "edit_profile"
}

fun NavGraphBuilder.editProfileRoute(
    onNavigateClick: () -> Unit,
) {
    composable(route = EditProfileNavigation.route) {
        EditProfileScreen(onNavigateClick = onNavigateClick)
    }
}


fun NavHostController.navigateToEditProfileRoute() {
    navigate(route = EditProfileNavigation.route)
}
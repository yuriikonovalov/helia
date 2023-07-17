package com.yuriikonovalov.helia.presentation.auth.signup

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

object SignUpNavigation {
    const val route = "sign_up"
}

fun NavGraphBuilder.signUpRoute(
    onNavigateClick: () -> Unit,
    onSignIn: () -> Unit,
    onFillProfile: () -> Unit
) {
    composable(route = SignUpNavigation.route) {
        SignUpScreen(
            onNavigateClick = onNavigateClick,
            onSignIn = onSignIn,
            onFillProfile = onFillProfile
        )
    }
}

fun NavHostController.navigateToSignUpRoute(navOptions: NavOptions? = null) {
    navigate(route = SignUpNavigation.route, navOptions = navOptions)
}
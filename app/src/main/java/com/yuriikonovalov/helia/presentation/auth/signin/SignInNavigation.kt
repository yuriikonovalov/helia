package com.yuriikonovalov.helia.presentation.auth.signin

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

object SignInNavigation {
    const val route = "sign_in"
}

fun NavGraphBuilder.signInRoute(
    onNavigateClick: () -> Unit,
    onSignUp: () -> Unit,
    onForgotPassword: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    composable(route = SignInNavigation.route) {
        SignInScreen(
            onNavigateClick = onNavigateClick,
            onSignUp = onSignUp,
            onForgotPassword = onForgotPassword,
            onNavigateToHome = onNavigateToHome
        )
    }
}

fun NavHostController.navigateToSignInRoute(navOptions: NavOptions? = null) {
    navigate(route = SignInNavigation.route, navOptions = navOptions)
}
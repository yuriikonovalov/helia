package com.yuriikonovalov.helia.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.yuriikonovalov.helia.presentation.auth.authentication.AuthenticationNavigation
import com.yuriikonovalov.helia.presentation.auth.authentication.authenticationRoute
import com.yuriikonovalov.helia.presentation.auth.authentication.navigateToAuthenticationRoute
import com.yuriikonovalov.helia.presentation.auth.signin.navigateToSignInRoute
import com.yuriikonovalov.helia.presentation.auth.signin.signInRoute
import com.yuriikonovalov.helia.presentation.auth.fillprofile.navigateToFillProfileRoute
import com.yuriikonovalov.helia.presentation.auth.fillprofile.fillProfileRoute
import com.yuriikonovalov.helia.presentation.auth.signup.navigateToSignUpRoute
import com.yuriikonovalov.helia.presentation.auth.signup.signUpRoute
import com.yuriikonovalov.helia.presentation.booking.bookingRoute
import com.yuriikonovalov.helia.presentation.home.homeRoute
import com.yuriikonovalov.helia.presentation.home.mybookmark.myBookmarkRoute
import com.yuriikonovalov.helia.presentation.home.mybookmark.navigateToMyBookmarkRoute
import com.yuriikonovalov.helia.presentation.home.navigateToHomeRoute
import com.yuriikonovalov.helia.presentation.home.recentlybooked.navigateToRecentlyBookedRoute
import com.yuriikonovalov.helia.presentation.home.recentlybooked.recentlyBookedRoute
import com.yuriikonovalov.helia.presentation.hoteldetails.gallery.hotelGalleryRoute
import com.yuriikonovalov.helia.presentation.hoteldetails.gallery.navigateToHotelGallery
import com.yuriikonovalov.helia.presentation.hoteldetails.hotelDetailsRoute
import com.yuriikonovalov.helia.presentation.hoteldetails.navigateToHotelDetailsRoute
import com.yuriikonovalov.helia.presentation.hoteldetails.review.hotelReviewsRoute
import com.yuriikonovalov.helia.presentation.hoteldetails.review.navigateToHotelReviews
import com.yuriikonovalov.helia.presentation.profile.edit.editProfileRoute
import com.yuriikonovalov.helia.presentation.profile.edit.navigateToEditProfileRoute
import com.yuriikonovalov.helia.presentation.profile.profileRoute
import com.yuriikonovalov.helia.presentation.profile.security.changepassword.changePasswordRoute
import com.yuriikonovalov.helia.presentation.profile.security.changepassword.navigateToChangePasswordRoute
import com.yuriikonovalov.helia.presentation.profile.security.navigateToProfileSecurityRoute
import com.yuriikonovalov.helia.presentation.profile.security.profileSecurityRoute
import com.yuriikonovalov.helia.presentation.search.navigateToSearchRoute
import com.yuriikonovalov.helia.presentation.search.searchRoute
import com.yuriikonovalov.helia.presentation.welcome.carousel.carouselRoute
import com.yuriikonovalov.helia.presentation.welcome.carousel.navigateToCarouselRoute
import com.yuriikonovalov.helia.presentation.welcome.welcomeRoute

@Composable
fun HeliaNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        welcomeRoute(onNavigateFurther = navController::navigateToCarouselRoute)
        carouselRoute(onNavigateFurther = navController::navigateToAuthenticationRoute)
        authenticationRoute(
            onSignIn = navController::navigateToSignInRoute,
            onSignUn = navController::navigateToSignUpRoute,
//            onNavigateToFillProfile = navController::navigateToSignUpFillProfileRoute
            onNavigateToFillProfile = {
                navController.navigateToFillProfileRoute(navOptions {
                    popUpTo(AuthenticationNavigation.route) {
                        inclusive = true
                    }
                })
            }
        )

        signInRoute(
            onNavigateClick = navController::navigateUp,
            onSignUp = {
                navController.navigateToSignUpRoute(navOptions = navOptions {
                    popUpTo(AuthenticationNavigation.route)
                })
            },
            onForgotPassword = {},
            onNavigateToHome = navController::navigateToHomeRoute
        )
        signUpRoute(
            onNavigateClick = navController::navigateUp,
            onFillProfile = navController::navigateToFillProfileRoute,
            onSignIn = {
                navController.navigateToSignInRoute(navOptions = navOptions {
                    popUpTo(AuthenticationNavigation.route)
                })
            }
        )
        fillProfileRoute(
            onNavigateClick = navController::navigateUp
//            onNavigateClick = {
//                navController.navigateToHomeRoute(
//                    TopLevelDestinationNavigationAction.navOptions(navController)
//                )
//            }
        )

        homeRoute(
            onBookmarksClick = navController::navigateToMyBookmarkRoute,
            onNotificationsClick = {},
            onSearchClick = navController::navigateToSearchRoute,
            onSeeAllBookedHotelsClick = navController::navigateToRecentlyBookedRoute,
            onHotelClick = navController::navigateToHotelDetailsRoute
        )
        recentlyBookedRoute(
            onNavigateClick = navController::navigateUp,
            onHotelClick = navController::navigateToHotelDetailsRoute
        )
        myBookmarkRoute(
            onNavigateClick = navController::navigateUp,
            onHotelClick = navController::navigateToHotelDetailsRoute
        )


        searchRoute(onHotelClick = navController::navigateToHotelDetailsRoute)
        bookingRoute()

        profileRoute(
            onEditProfileClick = navController::navigateToEditProfileRoute,
            onSecurityClick = navController::navigateToProfileSecurityRoute
        )
        // Profile top level destination
        editProfileRoute(onNavigateClick = navController::navigateUp)
        profileSecurityRoute(
            onNavigateClick = navController::navigateUp,
            onChangePasswordClick = navController::navigateToChangePasswordRoute
        )
        changePasswordRoute(onNavigateUp = navController::navigateUp)


        hotelDetailsRoute(
            onNavigationClick = navController::navigateUp,
            onGalleryClick = navController::navigateToHotelGallery,
            onMapClick = {},
            onReviewsClick = navController::navigateToHotelReviews
        )

        hotelReviewsRoute(onNavigationClick = navController::navigateUp)
        hotelGalleryRoute(onNavigationClick = navController::navigateUp)
    }
}
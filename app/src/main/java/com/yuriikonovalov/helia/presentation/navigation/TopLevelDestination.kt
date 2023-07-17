package com.yuriikonovalov.helia.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.yuriikonovalov.helia.R
import com.yuriikonovalov.helia.presentation.booking.navigateToBookingRoute
import com.yuriikonovalov.helia.presentation.home.navigateToHomeRoute
import com.yuriikonovalov.helia.presentation.profile.navigateToProfileRoute
import com.yuriikonovalov.helia.presentation.search.navigateToSearchRoute

enum class TopLevelDestination(
    @StringRes val labelResId: Int,
    @DrawableRes val selectedIconResId: Int,
    @DrawableRes val unselectedIconResId: Int
) {
    HOME(
        labelResId = R.string.destination_home,
        selectedIconResId = R.drawable.ic_home,
        unselectedIconResId = R.drawable.ic_home_border,
    ),
    SEARCH(
        labelResId = R.string.destination_search,
        selectedIconResId = R.drawable.ic_search,
        unselectedIconResId = R.drawable.ic_search_border
    ),
    BOOKING(
        labelResId = R.string.destination_booking,
        selectedIconResId = R.drawable.ic_document,
        unselectedIconResId = R.drawable.ic_document_border
    ),
    PROFILE(
        labelResId = R.string.destination_profile,
        selectedIconResId = R.drawable.ic_profile,
        unselectedIconResId = R.drawable.ic_profile_border
    )
}

class TopLevelDestinationNavigationAction(val navController: NavHostController) {
    fun navigate(destination: TopLevelDestination) {
        val options = navOptions(navController)
        when (destination) {
            TopLevelDestination.HOME -> navController.navigateToHomeRoute(options)
            TopLevelDestination.SEARCH -> navController.navigateToSearchRoute(options)
            TopLevelDestination.BOOKING -> navController.navigateToBookingRoute(options)
            TopLevelDestination.PROFILE -> navController.navigateToProfileRoute(options)
        }
    }

    companion object {
        fun navOptions(navController: NavHostController) = navOptions {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }
}

@Composable
fun rememberTopLevelDestinationNavigationAction(navController: NavHostController): TopLevelDestinationNavigationAction {
    return remember(navController) {
        TopLevelDestinationNavigationAction(navController)
    }
}

fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination): Boolean {
    return this?.hierarchy?.any {
        it.route?.contains(destination.name, ignoreCase = true) ?: false
    } ?: false
}
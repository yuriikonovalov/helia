package com.yuriikonovalov.helia.presentation.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yuriikonovalov.helia.designsystem.components.BottomNavigation
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme
import com.yuriikonovalov.helia.presentation.booking.BookingNavigation
import com.yuriikonovalov.helia.presentation.home.HomeNavigation
import com.yuriikonovalov.helia.presentation.profile.ProfileNavigation
import com.yuriikonovalov.helia.presentation.search.SearchNavigation

@Composable
fun HeliaNavigationWrapper(
    startDestination: String,
    modifier: Modifier = Modifier
) {

    val navController = rememberNavController()
    val destinationAction = rememberTopLevelDestinationNavigationAction(navController)
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    Scaffold(
        modifier = modifier,
        containerColor = HeliaTheme.backgroundColor,
        topBar = {},
        bottomBar = {
            if (currentDestination.isBottomBarVisible()) {
                BottomNavigation(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(vertical = 8.dp),
                    destinations = TopLevelDestination.values().toList(),
                    onNavigateToDestination = destinationAction::navigate,
                    currentDestination = currentDestination
                )
            }
        },
        content = { scaffoldPadding ->
            HeliaNavHost(
                modifier = Modifier.padding(bottom = scaffoldPadding.calculateBottomPadding()),
                navController = navController,
                startDestination = startDestination
            )
        }
    )
}


private fun NavDestination?.isBottomBarVisible(): Boolean {
    return this?.route in listOf(
        HomeNavigation.route,
        SearchNavigation.route,
        BookingNavigation.route,
        ProfileNavigation.route
    )
}
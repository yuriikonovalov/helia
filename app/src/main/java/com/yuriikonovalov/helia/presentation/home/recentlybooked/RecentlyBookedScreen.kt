package com.yuriikonovalov.helia.presentation.home.recentlybooked

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yuriikonovalov.helia.R
import com.yuriikonovalov.helia.designsystem.components.HotelDisplayMode
import com.yuriikonovalov.helia.designsystem.components.HotelSummaries
import com.yuriikonovalov.helia.designsystem.components.Navbar
import com.yuriikonovalov.helia.designsystem.components.NavbarAction
import com.yuriikonovalov.helia.presentation.utils.getIconResWithCurrentMode
import com.yuriikonovalov.helia.presentation.utils.getTintWithCurrentMode

@Composable
fun RecentlyBookedScreen(
    onNavigateClick: () -> Unit,
    onHotelClick: (hotelId: String) -> Unit,
    viewModel: RecentlyBookedScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    RecentlyBookedScreenContent(
        modifier = Modifier.fillMaxSize(),
        state = state,
        onNavigateClick = onNavigateClick,
        onHotelClick = onHotelClick,
        onBookmarkHotelClick = { id, isBookmarked ->
            viewModel.handleIntent(
                RecentlyBookedIntent.BookmarkHotel(hotelId = id, isBookmarked = isBookmarked)
            )
        },
        onListClick = {
            viewModel.handleIntent(
                RecentlyBookedIntent.ChangeViewMode(HotelDisplayMode.LIST)
            )
        },
        onGridClick = {
            viewModel.handleIntent(
                RecentlyBookedIntent.ChangeViewMode(HotelDisplayMode.GRID)
            )
        })
}

@Composable
private fun RecentlyBookedScreenContent(
    state: RecentlyBookedUiState,
    onNavigateClick: () -> Unit,
    onHotelClick: (hotelId: String) -> Unit,
    onBookmarkHotelClick: (hotelId: String, isBookmarked: Boolean) -> Unit,
    onListClick: () -> Unit,
    onGridClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        RecentlyBookedNavbar(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 24.dp, start = 8.dp, end = 8.dp),
            hotelDisplayMode = state.hotelDisplayMode,
            onNavigateClick = onNavigateClick,
            onListClick = onListClick,
            onGridClick = onGridClick
        )
        HotelSummaries(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .weight(1f),
            isLoading = state.isLoading,
            hotelDisplayMode = state.hotelDisplayMode,
            hotels = state.hotels,
            onHotelClick = onHotelClick,
            onBookmarkHotelClick = onBookmarkHotelClick
        )

    }
}

@Composable
private fun RecentlyBookedNavbar(
    hotelDisplayMode: HotelDisplayMode,
    onNavigateClick: () -> Unit,
    onListClick: () -> Unit,
    onGridClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Navbar(
        modifier = modifier,
        title = stringResource(id = R.string.recently_booked_screen_title),
        onNavigateClick = onNavigateClick
    ) {
        NavbarAction(
            iconResId = HotelDisplayMode.LIST.getIconResWithCurrentMode(hotelDisplayMode),
            onClick = onListClick,
            contentDescription = stringResource(R.string.recently_booked_screen_cd_list),
            tint = HotelDisplayMode.LIST.getTintWithCurrentMode(hotelDisplayMode)
        )
        NavbarAction(
            iconResId = HotelDisplayMode.GRID.getIconResWithCurrentMode(hotelDisplayMode),
            onClick = onGridClick,
            contentDescription = stringResource(R.string.recently_booked_screen_cd_grid),
            tint = HotelDisplayMode.GRID.getTintWithCurrentMode(hotelDisplayMode)
        )
    }
}



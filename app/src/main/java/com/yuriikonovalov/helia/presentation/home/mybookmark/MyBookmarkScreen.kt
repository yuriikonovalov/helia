package com.yuriikonovalov.helia.presentation.home.mybookmark

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yuriikonovalov.helia.R
import com.yuriikonovalov.helia.designsystem.components.Divider
import com.yuriikonovalov.helia.designsystem.components.HotelDisplayMode
import com.yuriikonovalov.helia.designsystem.components.HotelSummaries
import com.yuriikonovalov.helia.designsystem.components.ListHotelCard
import com.yuriikonovalov.helia.designsystem.components.Navbar
import com.yuriikonovalov.helia.designsystem.components.NavbarAction
import com.yuriikonovalov.helia.designsystem.components.PrimaryButton
import com.yuriikonovalov.helia.designsystem.components.SecondaryButton
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme
import com.yuriikonovalov.helia.domain.entities.HotelSummary
import com.yuriikonovalov.helia.presentation.utils.getIconResWithCurrentMode
import com.yuriikonovalov.helia.presentation.utils.getTintWithCurrentMode
import kotlinx.coroutines.launch

@Composable
fun MyBookmarkScreen(
    onNavigateClick: () -> Unit,
    onHotelClick: (hotelId: String) -> Unit,
    viewModel: MyBookmarkScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    RecentlyBookedScreenContent(
        modifier = Modifier.fillMaxSize(),
        state = state,
        onNavigateClick = onNavigateClick,
        onHotelClick = onHotelClick,
        onBookmarkHotelClick = { id, _ ->
            viewModel.handleIntent(MyBookmarkIntent.OpenBottomSheet(hotelId = id))
        },
        onListClick = {
            viewModel.handleIntent(MyBookmarkIntent.ChangeViewMode(HotelDisplayMode.LIST))
        },
        onGridClick = {
            viewModel.handleIntent(MyBookmarkIntent.ChangeViewMode(HotelDisplayMode.GRID))
        },
        onDismissBottomSheetRequest = { viewModel.handleIntent(MyBookmarkIntent.HideBottomSheet) },
        onDeleteBookmark = { id ->
            viewModel.handleIntent(MyBookmarkIntent.DeleteBookmark(hotelId = id))
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecentlyBookedScreenContent(
    state: MyBookmarkUiState,
    onNavigateClick: () -> Unit,
    onHotelClick: (hotelId: String) -> Unit,
    onBookmarkHotelClick: (hotelId: String, isBookmarked: Boolean) -> Unit,
    onListClick: () -> Unit,
    onGridClick: () -> Unit,
    onDismissBottomSheetRequest: () -> Unit,
    onDeleteBookmark: (hotelId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val hotel = state.hotelToDelete
    if (hotel != null) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = onDismissBottomSheetRequest,
            sheetState = bottomSheetState,
            windowInsets = WindowInsets(0),
            containerColor = HeliaTheme.backgroundColor
        ) {
            RemoveBookmarkBottomSheet(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 24.dp)
                    .navigationBarsPadding(),
                hotel = hotel,
                onCancelClick = {
                    coroutineScope
                        .launch { bottomSheetState.hide() }
                        .invokeOnCompletion { onDismissBottomSheetRequest() }
                },
                onRemoveClick = {
                    coroutineScope
                        .launch { bottomSheetState.hide() }
                        .invokeOnCompletion { onDeleteBookmark(hotel.hotel.id) }
                }
            )
        }
    }


    Column(modifier = modifier) {
        MyBookmarkNavbar(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 24.dp, start = 8.dp, end = 8.dp),
            hotelDisplayMode = state.hotelDisplayMode,
            onNavigateClick = onNavigateClick,
            onListClick = onListClick,
            onGridClick = onGridClick
        )
        DeletingProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            isDeleting = state.isDeleting
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
            onBookmarkHotelClick = { id, _ -> onBookmarkHotelClick(id, true) }
        )
    }
}

@Composable
private fun RemoveBookmarkBottomSheet(
    hotel: HotelSummary,
    onCancelClick: () -> Unit,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.my_bookmark_screen_remove_from_bookmark),
            style = HeliaTheme.typography.heading4,
            textAlign = TextAlign.Center,
            color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.white else HeliaTheme.colors.greyscale900
        )

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
        )

        ListHotelCard(
            imageUrl = hotel.hotel.imageUrl,
            name = hotel.hotel.name,
            city = "$${hotel.hotel.city}, ${hotel.hotel.country}",
            rating = hotel.displayRating,
            reviews = stringResource(id = R.string.number_of_reviews, hotel.hotel.numberOfReviews),
            pricePerNight = hotel.displayPrice,
            bookmarked = true,
            onClick = { },
            onBookmarkClick = { }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
        ) {
            SecondaryButton(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.cancel),
                onClick = onCancelClick
            )
            Spacer(modifier = Modifier.width(12.dp))
            PrimaryButton(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.my_bookmark_screen_remove_button),
                onClick = onRemoveClick
            )
        }
    }
}

@Composable
private fun DeletingProgressIndicator(
    isDeleting: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = isDeleting
    ) {
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            color = HeliaTheme.colors.primary500
        )
    }
}

@Composable
private fun MyBookmarkNavbar(
    hotelDisplayMode: HotelDisplayMode,
    onNavigateClick: () -> Unit,
    onListClick: () -> Unit,
    onGridClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Navbar(
        modifier = modifier,
        title = stringResource(id = R.string.my_bookmark_screen_title),
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
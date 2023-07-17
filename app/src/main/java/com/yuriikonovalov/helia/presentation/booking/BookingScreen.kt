package com.yuriikonovalov.helia.presentation.booking

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.yuriikonovalov.helia.R
import com.yuriikonovalov.helia.designsystem.components.Chip
import com.yuriikonovalov.helia.designsystem.components.Divider
import com.yuriikonovalov.helia.designsystem.components.PrimaryButton
import com.yuriikonovalov.helia.designsystem.components.SecondaryButton
import com.yuriikonovalov.helia.designsystem.components.Tag
import com.yuriikonovalov.helia.designsystem.components.TagStyle
import com.yuriikonovalov.helia.designsystem.components.TopDestinationNavbar
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme
import com.yuriikonovalov.helia.domain.entities.BookedHotel
import com.yuriikonovalov.helia.domain.entities.Hotel
import com.yuriikonovalov.helia.domain.valueobjects.BookingStatus
import kotlinx.coroutines.launch

@Composable
fun BookingScreen(
    viewModel: BookingScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BookingScreenContent(
        modifier = Modifier.fillMaxSize(),
        state = state,
        onCancelBookingConfirmed = {
            viewModel.handleIntent(BookingScreenIntent.ConfirmBookingCanceling(it))
        },
        onChangeStatusFilter = {
            viewModel.handleIntent(BookingScreenIntent.ChangeBookingStatusFiler(it))
        },
        onDismissBottomSheetRequest = {
            viewModel.handleIntent(BookingScreenIntent.HideBottomSheet)
        },
        onCancelClick = {
            // open a confirmation bottom sheet
            viewModel.handleIntent(BookingScreenIntent.CancelBooking(it))
        },
        onViewTicketClick = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookingScreenContent(
    state: BookingScreenUiState,
    onCancelBookingConfirmed: (hotel: Hotel) -> Unit,
    onChangeStatusFilter: (status: BookingStatus) -> Unit,
    modifier: Modifier = Modifier,
    onDismissBottomSheetRequest: () -> Unit,
    onCancelClick: (hotel: Hotel) -> Unit,
    onViewTicketClick: (hotelId: String) -> Unit
) {


    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val hotel = state.hotelToCancel

    BackHandler(enabled = hotel != null) {
        coroutineScope
            .launch { bottomSheetState.hide() }
            .invokeOnCompletion { onDismissBottomSheetRequest() }
    }

    if (hotel != null) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = onDismissBottomSheetRequest,
            sheetState = bottomSheetState,
            windowInsets = WindowInsets(0),
            containerColor = HeliaTheme.backgroundColor
        ) {
            CancelBookingBottomSheet(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 24.dp)
                    .navigationBarsPadding(),
                onCancelClick = {
                    coroutineScope
                        .launch { bottomSheetState.hide() }
                        .invokeOnCompletion { onDismissBottomSheetRequest() }
                },
                onContinueClick = {
                    coroutineScope
                        .launch { bottomSheetState.hide() }
                        .invokeOnCompletion { onCancelBookingConfirmed(hotel) }
                }
            )
        }
    }


    Column(modifier = modifier) {
        MyBookingTopBar(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(24.dp)
        )

        BookingStatusFilter(
            modifier = Modifier.fillMaxWidth(),
            currentStatus = state.bookingStatusFilter,
            onStatusClick = onChangeStatusFilter
        )

        CancelProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            isCanceling = state.isLoading
        )
        BookedHotels(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .padding(horizontal = 24.dp)
                .weight(1f),
            isLoading = state.isLoading,
            hotels = state.hotelsToDisplay,
            onCancelClick = onCancelClick,
            onViewTicketClick = onViewTicketClick
        )
    }
}

@Composable
private fun BookingStatusFilter(
    modifier: Modifier,
    currentStatus: BookingStatus,
    onStatusClick: (status: BookingStatus) -> Unit
) {
    val items = remember { BookingStatus.values() }

    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = items, key = { it.ordinal }) { status ->
            Chip(
                toggled = status == currentStatus,
                onClick = { onStatusClick(status) },
                text = stringResource(id = status.asStringRes())
            )
        }
    }
}

@Composable
private fun BookedHotels(
    modifier: Modifier,
    isLoading: Boolean,
    hotels: List<BookedHotel>,
    onCancelClick: (hotel: Hotel) -> Unit,
    onViewTicketClick: (hotelId: String) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(vertical = 20.dp)
    ) {
        items(items = hotels, key = { it.hotel.id }) { bookedHotel ->
            BookedHotelListItem(
                modifier = Modifier.fillMaxWidth(),
                name = bookedHotel.hotel.name,
                address = bookedHotel.address,
                imageUrl = bookedHotel.hotel.imageUrl,
                status = bookedHotel.status,
                onCancelClick = { onCancelClick(bookedHotel.hotel) },
                onViewTicketClick = { onViewTicketClick(bookedHotel.hotel.id) }
            )
        }
    }
}

@Composable
private fun BookedHotelListItem(
    modifier: Modifier,
    name: String,
    address: String,
    imageUrl: String,
    status: BookingStatus,
    onCancelClick: () -> Unit,
    onViewTicketClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = HeliaTheme.shapes.medium,
        shadowElevation = 1.dp,
        color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.dark2 else HeliaTheme.colors.white
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            BookedHotelListItemDetails(
                modifier = Modifier.fillMaxWidth(),
                name = name,
                address = address,
                status = status,
                imageUrl = imageUrl
            )
            Divider(modifier = Modifier.fillMaxWidth())
            if (status == BookingStatus.ONGOING) {
                BookedHotelListItemButtons(
                    modifier = Modifier.fillMaxWidth(),
                    onCancelClick = onCancelClick,
                    onViewTicketClick = onViewTicketClick
                )
            } else {
                Tag(
                    text = stringResource(id = status.asBodyStringRes()),
                    style = TagStyle.INVERTED,
                    state = status.asTagState()
                )
            }
        }

    }
}

@Composable
private fun BookedHotelListItemButtons(
    modifier: Modifier,
    onCancelClick: () -> Unit,
    onViewTicketClick: () -> Unit
) {
    val buttonContentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SecondaryButton(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.my_booking_screen_cancel_booking),
            onClick = onCancelClick,
            contentPadding = buttonContentPadding
        )

        PrimaryButton(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.my_booking_scree_view_ticket),
            onClick = onViewTicketClick,
            contentPadding = buttonContentPadding
        )
    }
}

@Composable
private fun BookedHotelListItemDetails(
    modifier: Modifier,
    name: String,
    address: String,
    status: BookingStatus,
    imageUrl: String
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        AsyncImage(
            modifier = Modifier
                .size(100.dp)
                .clip(HeliaTheme.shapes.small),
            model = imageUrl,
            contentDescription = name,
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = name,
                style = HeliaTheme.typography.heading5,
                color = HeliaTheme.primaryTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = address,
                style = HeliaTheme.typography.bodyMediumRegular,
                color = HeliaTheme.secondaryTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Tag(
                text = stringResource(id = status.asLabelStringRes()),
                style = TagStyle.INVERTED,
                state = status.asTagState()
            )
        }
    }
}

@Composable
private fun CancelProgressIndicator(
    modifier: Modifier,
    isCanceling: Boolean
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = isCanceling,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            color = HeliaTheme.colors.primary500
        )
    }
}

@Composable
private fun MyBookingTopBar(modifier: Modifier) {
    TopDestinationNavbar(
        modifier = modifier,
        title = stringResource(R.string.my_booking_screen_title),
        actions = {}
    )
}

@Composable
private fun CancelBookingBottomSheet(
    onCancelClick: () -> Unit,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.my_booking_screen_cancel_booking_title),
            style = HeliaTheme.typography.heading4,
            textAlign = TextAlign.Center,
            color = HeliaTheme.colors.error
        )

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.my_booking_screen_cancel_booking_subtitle),
            style = HeliaTheme.typography.bodyLargeBold,
            textAlign = TextAlign.Center,
            color = HeliaTheme.primaryTextColor
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            text = stringResource(R.string.my_booking_screen_cancel_booking_body),
            style = HeliaTheme.typography.bodySmallRegular,
            textAlign = TextAlign.Center,
            color = HeliaTheme.secondaryTextColor
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
                text = stringResource(id = R.string.my_booking_screen_cancel_booking_positive_button),
                onClick = onContinueClick
            )
        }
    }
}
package com.yuriikonovalov.helia.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yuriikonovalov.helia.R
import com.yuriikonovalov.helia.designsystem.components.Chip
import com.yuriikonovalov.helia.designsystem.components.ImageHotelCard
import com.yuriikonovalov.helia.designsystem.components.ListHotelCard
import com.yuriikonovalov.helia.designsystem.components.NavbarAction
import com.yuriikonovalov.helia.designsystem.components.SearchBar
import com.yuriikonovalov.helia.designsystem.components.TextButton
import com.yuriikonovalov.helia.designsystem.components.TopDestinationNavbar
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme
import com.yuriikonovalov.helia.domain.entities.HotelSummary
import com.yuriikonovalov.helia.domain.valueobjects.HotelCategory

@Composable
fun HomeScreen(
    onBookmarksClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onSearchClick: () -> Unit,
    onSeeAllBookedHotelsClick: () -> Unit,
    onPreviewHotelClick: (hotelId: String) -> Unit,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreenContent(
        onBookmarksClick = onBookmarksClick,
        onNotificationsClick = onNotificationsClick,
        modifier = Modifier.fillMaxSize(),
        state = state,
        onSearchClick = onSearchClick,
        onHotelCategoryClick = { viewModel.handleIntent(HomeIntent.ChangeHotelCategory(it)) },
        onPreviewHotelClick = onPreviewHotelClick,
        onBookmarkHotelClick = { id, isBookmarked ->
            viewModel.handleIntent(HomeIntent.BookmarkHotel(id, isBookmarked))
        },
        onSeeAllBookedHotelsClick = onSeeAllBookedHotelsClick
    )
}

@Composable
private fun HomeScreenContent(
    onBookmarksClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    modifier: Modifier = Modifier,
    state: HomeUiState,
    onSearchClick: () -> Unit,
    onHotelCategoryClick: (category: HotelCategory) -> Unit,
    onPreviewHotelClick: (hotelId: String) -> Unit,
    onBookmarkHotelClick: (hotelId: String, isBookmarked: Boolean) -> Unit,
    onSeeAllBookedHotelsClick: () -> Unit
) {
    Column(modifier = modifier) {
        HomeNavbar(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 24.dp, start = 24.dp, end = 8.dp),
            onBookmarksClick = onBookmarksClick,
            onNotificationsClick = onNotificationsClick
        )
        ScreenContent(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            onSearchClick = onSearchClick,
            onHotelCategoryClick = onHotelCategoryClick,
            onPreviewHotelClick = onPreviewHotelClick,
            onBookmarkHotelClick = onBookmarkHotelClick,
            onSeeAllBookedHotelsClick = onSeeAllBookedHotelsClick
        )

    }
}

@Composable
private fun HomeNavbar(
    onBookmarksClick: () -> Unit, onNotificationsClick: () -> Unit, modifier: Modifier = Modifier
) {
    TopDestinationNavbar(
        modifier = modifier, title = stringResource(id = R.string.app_name)
    ) {
        NavbarAction(
            iconResId = R.drawable.ic_notification_border,
            onClick = onNotificationsClick,
            contentDescription = stringResource(R.string.home_screen_cd_notifications)
        )
        NavbarAction(
            iconResId = R.drawable.ic_bookmark_border,
            onClick = onBookmarksClick,
            contentDescription = stringResource(R.string.home_screen_cd_my_bookmarks)
        )
    }
}

@Composable
private fun ScreenContent(
    state: HomeUiState,
    modifier: Modifier = Modifier,
    onSearchClick: () -> Unit,
    onHotelCategoryClick: (category: HotelCategory) -> Unit,
    onPreviewHotelClick: (hotelId: String) -> Unit,
    onBookmarkHotelClick: (hotelId: String, isBookmarked: Boolean) -> Unit,
    onSeeAllBookedHotelsClick: () -> Unit,
) {
    Column(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Greeting(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp),
            name = state.userName
        )
        Search(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp), onClick = onSearchClick
        )
        HotelCategory(
            modifier = Modifier.fillMaxWidth(),
            selectedCategory = state.hotelCategory,
            onClick = onHotelCategoryClick
        )
        HotelRow(
            modifier = Modifier.fillMaxWidth(),
            isLoading = state.isHotelByCategoryLoading,
            hotels = state.hotels,
            onPreviewHotelClick = onPreviewHotelClick,
            onBookmarkHotelClick = onBookmarkHotelClick
        )

        RecentlyBooked(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            hotels = state.recentlyBookedHotels,
            onPreviewHotelClick = onPreviewHotelClick,
            onBookmarkHotelClick = onBookmarkHotelClick,
            onSeeAllClick = onSeeAllBookedHotelsClick
        )

    }
}

@Composable
private fun Greeting(
    name: String?, modifier: Modifier = Modifier
) {
    val text = if (name.isNullOrBlank()) {
        stringResource(id = R.string.home_screen_greeting)
    } else {
        stringResource(id = R.string.home_screen_greeting_with_name, name)
    }
    Text(
        modifier = modifier,
        text = text,
        style = HeliaTheme.typography.heading4,
        color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.white else HeliaTheme.colors.greyscale900
    )
}

@Composable
private fun Search(
    onClick: () -> Unit, modifier: Modifier = Modifier
) {
    SearchBar(
        value = "",
        onValueChanged = {},
        onSearchKeyboardAction = { },
        modifier = modifier,
        onClick = onClick
    )
}

@Composable
private fun HotelCategory(
    selectedCategory: HotelCategory,
    onClick: (category: HotelCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = remember { HotelCategory.values() }

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 24.dp)
    ) {
        items(items = categories, key = { item -> item.ordinal }) { category ->
            val toggled = category == selectedCategory
            Chip(
                text = stringResource(category.stringResId),
                toggled = toggled,
                onClick = { onClick(category) },
            )
        }
    }
}

@Composable
private fun ColumnScope.HotelRow(
    isLoading: Boolean,
    hotels: List<HotelSummary>,
    onPreviewHotelClick: (hotelId: String) -> Unit,
    onBookmarkHotelClick: (hotelId: String, isBookmarked: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    if (isLoading) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = HeliaTheme.colors.primary500)
                Text(
                    modifier = Modifier.padding(top = 32.dp),
                    text = stringResource(R.string.home_screen_loading),
                    textAlign = TextAlign.Center,
                    style = HeliaTheme.typography.bodyLargeRegular,
                    color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.white else HeliaTheme.colors.greyscale900
                )
            }
        }
    } else {
        LazyRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(horizontal = 24.dp)
        ) {
            items(items = hotels, key = { item -> item.hotel.id }) { item ->
                ImageHotelCard(imageUrl = item.hotel.imageUrl,
                    name = item.hotel.name,
                    city = "${item.hotel.city}, ${item.hotel.country}",
                    pricePerNight = item.displayPrice,
                    rating = item.displayRating,
                    bookmarked = item.isBookmarked,
                    onBookmarkClick = { onBookmarkHotelClick(item.hotel.id, item.isBookmarked) },
                    onClick = { onPreviewHotelClick(item.hotel.id) })
            }
        }
    }
}


@Composable
private fun RecentlyBooked(
    hotels: List<HotelSummary>,
    onSeeAllClick: () -> Unit,
    onPreviewHotelClick: (hotelId: String) -> Unit,
    onBookmarkHotelClick: (hotelId: String, isBookmarked: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    if (hotels.isNotEmpty())
        Column(
            modifier = modifier, verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            RecentlyBookedHeader(
                modifier = Modifier.fillMaxWidth(), onSeeAllClick = onSeeAllClick
            )
            RecentlyBookedHotels(
                modifier = Modifier.fillMaxWidth(),
                hotels = hotels,
                onPreviewHotelClick = onPreviewHotelClick,
                onBookmarkHotelClick = onBookmarkHotelClick
            )
        }
}

@Composable
private fun RecentlyBookedHeader(
    onSeeAllClick: () -> Unit, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.home_screen_recently_booked),
            style = HeliaTheme.typography.heading6,
            color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.white else HeliaTheme.colors.greyscale900
        )
        TextButton(
            text = stringResource(R.string.see_all_button),
            onClick = onSeeAllClick
        )
    }
}

@Composable
private fun RecentlyBookedHotels(
    hotels: List<HotelSummary>,
    onPreviewHotelClick: (hotelId: String) -> Unit,
    onBookmarkHotelClick: (hotelId: String, isBookmarked: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    // We intentionally don't use LazyColumn here in order to avoid a nested scrolling exception.
    // This list will contain 5 items at most.
    Column(
        modifier = modifier.padding(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        hotels.forEach { item ->
            ListHotelCard(imageUrl = item.hotel.imageUrl,
                name = item.hotel.name,
                city = "${item.hotel.city}, ${item.hotel.country}",
                pricePerNight = item.displayPrice,
                rating = item.displayRating,
                reviews = stringResource(R.string.number_of_reviews, item.hotel.numberOfReviews),
                bookmarked = item.isBookmarked,
                onBookmarkClick = { onBookmarkHotelClick(item.hotel.id, item.isBookmarked) },
                onClick = { onPreviewHotelClick(item.hotel.id) })
        }
    }
}
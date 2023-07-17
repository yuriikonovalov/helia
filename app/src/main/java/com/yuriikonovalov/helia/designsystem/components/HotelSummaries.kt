package com.yuriikonovalov.helia.designsystem.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yuriikonovalov.helia.R
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme
import com.yuriikonovalov.helia.domain.entities.HotelSummary

@Composable
fun HotelSummaries(
    isLoading: Boolean,
    hotelDisplayMode: HotelDisplayMode,
    hotels: List<HotelSummary>,
    onHotelClick: (hotelId: String) -> Unit,
    modifier: Modifier = Modifier,
    onBookmarkHotelClick: (hotelId: String, isBookmarked: Boolean) -> Unit
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
        AnimatedContent(
            modifier = modifier,
            targetState = hotelDisplayMode,
            label = ""
        ) { viewMode ->
            if (viewMode == HotelDisplayMode.LIST) {
                HotelList(
                    modifier = Modifier.fillMaxWidth(),
                    hotels = hotels,
                    onHotelClick = onHotelClick,
                    onBookmarkHotelClick = onBookmarkHotelClick
                )
            } else {
                HotelGrid(
                    modifier = Modifier.fillMaxWidth(),
                    hotels = hotels,
                    onHotelClick = onHotelClick,
                    onBookmarkHotelClick = onBookmarkHotelClick
                )
            }
        }
    }
}


@Composable
private fun HotelList(
    hotels: List<HotelSummary>,
    onHotelClick: (hotelId: String) -> Unit,
    onBookmarkHotelClick: (hotelId: String, isBookmarked: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(items = hotels, key = { it.hotel.id }) { item ->
            ListHotelCard(imageUrl = item.hotel.imageUrl,
                name = item.hotel.name,
                city = "${item.hotel.city}, ${item.hotel.country}",
                pricePerNight = item.displayPrice,
                rating = item.displayRating,
                reviews = stringResource(R.string.number_of_reviews, item.hotel.numberOfReviews),
                bookmarked = item.isBookmarked,
                onBookmarkClick = { onBookmarkHotelClick(item.hotel.id, item.isBookmarked) },
                onClick = { onHotelClick(item.hotel.id) })
        }
    }
}

@Composable
private fun HotelGrid(
    hotels: List<HotelSummary>,
    onHotelClick: (hotelId: String) -> Unit,
    onBookmarkHotelClick: (hotelId: String, isBookmarked: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(items = hotels, key = { it.hotel.id }) { item ->
            GridHotelCard(
                imageUrl = item.hotel.imageUrl,
                name = item.hotel.name,
                city = "${item.hotel.city}, ${item.hotel.country}",
                pricePerNight = item.displayPrice,
                rating = item.displayRating,
                bookmarked = item.isBookmarked,
                onBookmarkClick = { onBookmarkHotelClick(item.hotel.id, item.isBookmarked) },
                onClick = { onHotelClick(item.hotel.id) }
            )
        }
    }
}
package com.yuriikonovalov.helia.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.yuriikonovalov.helia.R
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme

private val gradientBrush = Brush.verticalGradient(listOf(Color(0x004B4B4B), Color(0xB3202020)))

@Composable
fun ImageHotelCard(
    imageUrl: String,
    name: String,
    city: String,
    pricePerNight: String,
    rating: String,
    bookmarked: Boolean,
    onClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.size(width = 300.dp, height = 400.dp),
        shape = HeliaTheme.shapes.extraLarge,
        shadowElevation = 2.dp,
        onClick = onClick
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = imageUrl,
            contentDescription = name,
            contentScale = ContentScale.Crop
        )
        Column {
            Chip(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(32.dp),
                toggled = true,
                onClick = { },
                text = rating,
                leadingIconResId = R.drawable.ic_star,
                chipSize = ChipSizeValues.Small
            )
            Spacer(modifier = Modifier.weight(1f))
            ImageHotelCardDetails(
                modifier = Modifier.fillMaxWidth(),
                name = name,
                city = city,
                pricePerNight = pricePerNight,
                bookmarked = bookmarked,
                onBookmarkClick = onBookmarkClick
            )
        }

    }
}

@Composable
private fun ImageHotelCardDetails(
    name: String,
    city: String,
    pricePerNight: String,
    bookmarked: Boolean,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.background(gradientBrush)) {
        Column(modifier = Modifier.padding(horizontal = 32.dp, vertical = 24.dp)) {
            Text(
                text = name,
                style = HeliaTheme.typography.heading4,
                color = HeliaTheme.colors.white,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = city,
                style = HeliaTheme.typography.bodyLargeRegular,
                color = HeliaTheme.colors.white,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        modifier = Modifier.alignByBaseline(),
                        text = pricePerNight,
                        style = HeliaTheme.typography.heading4,
                        color = HeliaTheme.colors.white
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        modifier = Modifier.alignByBaseline(),
                        text = stringResource(R.string.per_night),
                        style = HeliaTheme.typography.bodyMediumRegular,
                        color = HeliaTheme.colors.white
                    )
                }
                Icon(
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onBookmarkClick() },
                    painter = painterResource(
                        if (bookmarked) R.drawable.ic_bookmark else R.drawable.ic_bookmark_border
                    ),
                    contentDescription = "",
                    tint = if (bookmarked) HeliaTheme.colors.primary500 else HeliaTheme.colors.white
                )
            }
        }
    }
}


@Composable
fun ListHotelCard(
    imageUrl: String,
    name: String,
    city: String,
    rating: String,
    reviews: String,
    pricePerNight: String,
    bookmarked: Boolean,
    onClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(IntrinsicSize.Min),
        shape = HeliaTheme.shapes.medium,
        shadowElevation = 2.dp,
        color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.dark2 else HeliaTheme.colors.white,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(100.dp)
                    .clip(HeliaTheme.shapes.medium),
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            ListHotelCardDetails(
                modifier = Modifier.weight(1f),
                name = name,
                city = city,
                rating = rating,
                reviews = reviews
            )
            Spacer(modifier = Modifier.width(8.dp))
            ListHotelCardPriceAndBookmark(
                modifier = Modifier.fillMaxHeight(),
                pricePerNight = pricePerNight,
                bookmarked = bookmarked,
                onBookmarkClick = onBookmarkClick
            )
        }
    }
}

@Composable
private fun ListHotelCardDetails(
    name: String,
    city: String,
    rating: String,
    reviews: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Text(
            text = name,
            style = HeliaTheme.typography.heading5,
            color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.white else HeliaTheme.colors.greyscale900,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = city,
            style = HeliaTheme.typography.bodyMediumRegular,
            color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.greyscale300 else HeliaTheme.colors.greyscale700,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            HotelSimpleRating(rating = rating)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = reviews,
                style = HeliaTheme.typography.bodySmallRegular,
                color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.greyscale300 else HeliaTheme.colors.greyscale600,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun ListHotelCardPriceAndBookmark(
    pricePerNight: String,
    bookmarked: Boolean,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 6.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = pricePerNight,
                style = HeliaTheme.typography.heading4,
                color = HeliaTheme.colors.primary500
            )
            Text(
                text = stringResource(R.string.night),
                style = HeliaTheme.typography.bodyXSmallRegular,
                color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.greyscale300 else HeliaTheme.colors.greyscale700
            )
        }
        BookmarkIcon(bookmarked = bookmarked, onBookmarkClick = onBookmarkClick)
    }
}

@Composable
private fun HotelSimpleRating(
    rating: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(12.dp),
            painter = painterResource(id = R.drawable.ic_star),
            contentDescription = null,
            tint = HeliaTheme.colors.secondary500
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = rating,
            style = HeliaTheme.typography.bodyMediumSemiBold,
            color = HeliaTheme.colors.primary500
        )
    }
}

@Composable
private fun BookmarkIcon(
    bookmarked: Boolean,
    onBookmarkClick: () -> Unit
) {
    Icon(
        modifier = Modifier
            .requiredSize(24.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onBookmarkClick() },
        painter = painterResource(
            if (bookmarked) R.drawable.ic_bookmark else R.drawable.ic_bookmark_border
        ),
        contentDescription = "",
        tint = if (bookmarked) {
            HeliaTheme.colors.primary500
        } else if (HeliaTheme.theme.isDark) {
            HeliaTheme.colors.white
        } else {
            HeliaTheme.colors.greyscale800
        }
    )
}


@Composable
fun GridHotelCard(
    name: String,
    city: String,
    rating: String,
    pricePerNight: String,
    bookmarked: Boolean,
    imageUrl: String,
    onClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = HeliaTheme.shapes.medium,
        shadowElevation = 2.dp,
        color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.dark2 else HeliaTheme.colors.white,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .padding(16.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.16f)
                    .clip(HeliaTheme.shapes.medium),
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))
            GridHotelCardDetails(
                name = name,
                city = city,
                rating = rating,
                pricePerNight = pricePerNight,
                bookmarked = bookmarked,
                onBookmarkClick = onBookmarkClick
            )
        }
    }
}

@Composable
private fun GridHotelCardDetails(
    name: String,
    city: String,
    rating: String,
    pricePerNight: String,
    bookmarked: Boolean,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = name,
            style = HeliaTheme.typography.heading6,
            color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.white else HeliaTheme.colors.greyscale900,
            minLines = 2,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            HotelSimpleRating(rating = rating)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = city,
                style = HeliaTheme.typography.bodySmallRegular,
                color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.greyscale300 else HeliaTheme.colors.greyscale700,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

        }
        Spacer(modifier = Modifier.height(8.dp))
        GridHotelCardPriceAndBookmark(
            pricePerNight = pricePerNight,
            bookmarked = bookmarked,
            onBookmarkClick = onBookmarkClick
        )
    }
}

@Composable
private fun GridHotelCardPriceAndBookmark(
    pricePerNight: String,
    bookmarked: Boolean,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = pricePerNight,
                style = HeliaTheme.typography.heading5,
                color = HeliaTheme.colors.primary500
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(id = R.string.per_night),
                style = HeliaTheme.typography.bodyXSmallRegular,
                color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.greyscale300 else HeliaTheme.colors.greyscale700,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.requiredWidth(8.dp))
        BookmarkIcon(bookmarked = bookmarked, onBookmarkClick = onBookmarkClick)
    }
}


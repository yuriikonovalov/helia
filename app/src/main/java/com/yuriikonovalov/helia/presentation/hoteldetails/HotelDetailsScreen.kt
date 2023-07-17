package com.yuriikonovalov.helia.presentation.hoteldetails

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.yuriikonovalov.helia.R
import com.yuriikonovalov.helia.designsystem.components.Dialog
import com.yuriikonovalov.helia.designsystem.components.Divider
import com.yuriikonovalov.helia.designsystem.components.NavbarAction
import com.yuriikonovalov.helia.designsystem.components.PrimaryButton
import com.yuriikonovalov.helia.designsystem.components.ReviewCard
import com.yuriikonovalov.helia.designsystem.components.ReviewsLabel
import com.yuriikonovalov.helia.designsystem.components.SecondaryButton
import com.yuriikonovalov.helia.designsystem.components.SliderIndicator
import com.yuriikonovalov.helia.designsystem.components.TextButton
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme
import com.yuriikonovalov.helia.domain.entities.HotelDetails
import com.yuriikonovalov.helia.domain.valueobjects.HotelFacility
import com.yuriikonovalov.helia.presentation.utils.asDrawableRes
import com.yuriikonovalov.helia.presentation.utils.asStringRes
import com.yuriikonovalov.helia.presentation.utils.toUiString

@Composable
fun HotelDetailsScreen(
    onNavigationClick: () -> Unit,
    onGalleryClick: (hotelId: String) -> Unit,
    onMapClick: (hotelId: String) -> Unit,
    onReviewsClick: (hotelId: String) -> Unit,
    viewModel: HotelDetailsScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HotelDetailsScreenContent(
        modifier = Modifier.fillMaxWidth(),
        state = state,
        onNavigationClick = onNavigationClick,
        onGalleryClick = onGalleryClick,
        onReviewsClick = onReviewsClick,
        onBookmarkClick = { id, isBookmarked ->
            viewModel.handleIntent(
                HotelDetailsIntent.ClickBookmark(hotelId = id, isBookmarked = isBookmarked)
            )
        },
        onBookClick = { viewModel.handleIntent(HotelDetailsIntent.BookHotel(it)) }
    )
}

@Composable
private fun HotelDetailsScreenContent(
    state: HotelDetailsUiState,
    onNavigationClick: () -> Unit,
    onBookmarkClick: (hotelId: String, isBookmarked: Boolean) -> Unit,
    onGalleryClick: (hotelId: String) -> Unit,
    onReviewsClick: (hotelId: String) -> Unit,
    onBookClick: (hotelId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (state is HotelDetailsUiState.Success && state.isHotelBooked) {
        HotelBookedDialog(onDismiss = onNavigationClick)
    }

    AnimatedContent(
        modifier = modifier,
        targetState = state,
        label = ""
    ) { state ->
        when (state) {
            is HotelDetailsUiState.Success -> {
                HotelDetails(
                    modifier = Modifier.fillMaxWidth(),
                    details = state.hotelDetails,
                    isBookmarked = state.isBookmarked,
                    onNavigationClick = onNavigationClick,
                    onBookmarkClick = onBookmarkClick,
                    onGalleryClick = { onGalleryClick(state.hotelDetails.hotel.id) },
                    onReviewsClick = { onReviewsClick(state.hotelDetails.hotel.id) },
                    onBookClick = { onBookClick(state.hotelDetails.hotel.id) }
                )
            }

            HotelDetailsUiState.Error -> TODO()
            HotelDetailsUiState.Loading -> {
                LoadingContent(Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
private fun HotelBookedDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Image(
                modifier = Modifier.size(150.dp),
                painter = painterResource(id = R.drawable.il_payment_successful),
                contentDescription = null
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.payment_successfull),
                    style = HeliaTheme.typography.heading4,
                    textAlign = TextAlign.Center,
                    color = HeliaTheme.colors.primary500
                )
                Text(
                    text = stringResource(R.string.successfully_payment_body),
                    style = HeliaTheme.typography.bodyLargeRegular,
                    textAlign = TextAlign.Center,
                    color = HeliaTheme.primaryTextColor
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.view_ticket),
                    onClick = onDismiss
                )
                SecondaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.cancel),
                    onClick = onDismiss
                )
            }
        }
    }
}

@Composable
private fun LoadingContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = HeliaTheme.colors.primary500)
    }
}

@Composable
private fun HotelDetails(
    details: HotelDetails,
    isBookmarked: Boolean,
    onNavigationClick: () -> Unit,
    onBookmarkClick: (hotelId: String, isBookmarked: Boolean) -> Unit,
    onGalleryClick: () -> Unit,
    onReviewsClick: () -> Unit,
    onBookClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var imageHeight by remember { mutableIntStateOf(0) }
    var topBarHeight by remember { mutableIntStateOf(0) }

    val isTopBarVisible by remember {
        derivedStateOf {
            // changes to true when the bottom of the image is aligned (or further)
            // with the bottom of the top bar.
            scrollState.value > imageHeight - topBarHeight
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopStart
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .weight(1f)
            ) {
                ImageSlider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onSizeChanged { imageHeight = it.height },
                    images = details.photoUrls
                )

                NameAndAddress(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 24.dp),
                    name = details.hotel.name,
                    address = "${details.address.addressLine1}, ${details.address.city}, ${details.address.country}"
                )

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 32.dp)
                )

                Gallery(
                    modifier = Modifier.fillMaxWidth(),
                    images = details.photoUrls,
                    onSeeAllClick = onGalleryClick
                )

                Details(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp)
                        .padding(horizontal = 24.dp),
                    numberOfBedrooms = details.hotelInformation.numberOfBedrooms,
                    numberOfBathrooms = details.hotelInformation.numberOfBathrooms,
                    squareMeters = details.hotelInformation.squareMeters
                )

                Description(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp)
                        .padding(horizontal = 24.dp),
                    text = details.description
                )

                Facilities(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp)
                        .padding(horizontal = 24.dp),
                    facilities = details.facilities
                )

                Review(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp)
                        .padding(horizontal = 24.dp),
                    reviews = details.reviews,
                    numberOfReviews = details.hotel.numberOfReviews,
                    rating = details.hotel.rating,
                    onSeeAllClick = onReviewsClick
                )
            }
            BookPanel(
                modifier = Modifier.fillMaxWidth(),
                price = "$${details.hotel.price.toInt()}",
                onClick = onBookClick
            )
        }

        TopBar(
            modifier = Modifier.onSizeChanged { topBarHeight = it.height },
            visible = isTopBarVisible,
            title = details.hotel.name,
            isBookmarked = isBookmarked,
            onNavigationClick = onNavigationClick,
            onBookmarkClick = { onBookmarkClick(details.hotel.id, isBookmarked) }
        )
    }

}

@Composable
private fun TopBar(
    title: String,
    isBookmarked: Boolean,
    visible: Boolean,
    onNavigationClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val title = if (visible) title else ""

    val icon = if (isBookmarked) R.drawable.ic_bookmark else R.drawable.ic_bookmark_border

    val tint = if (visible && !HeliaTheme.theme.isDark)
        HeliaTheme.colors.greyscale900
    else
        HeliaTheme.colors.white

    val bookmarkIconTint = if (isBookmarked) HeliaTheme.colors.primary500 else tint


    Box(
        modifier = modifier
            .then(if (visible) Modifier.background(HeliaTheme.backgroundColor) else Modifier)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 24.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigationClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_left),
                    contentDescription = stringResource(R.string.cd_navigate_up),
                    tint = tint
                )
            }
            Text(
                modifier = Modifier.weight(1f),
                text = title,
                style = HeliaTheme.typography.heading4,
                color = tint,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.width(12.dp))
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.End
            ) {
                NavbarAction(
                    iconResId = icon,
                    onClick = onBookmarkClick,
                    contentDescription = "",
                    tint = bookmarkIconTint
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ImageSlider(
    images: List<String>,
    modifier: Modifier
) {
    val pagerState = rememberPagerState(pageCount = { images.size })

    Box(modifier = modifier.height(250.dp)) {
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState
        ) { page ->
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = images[page],
                contentScale = ContentScale.Crop,
                contentDescription = ""
            )
        }

        SliderIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            slides = images,
            currentSlide = images[pagerState.currentPage]
        )
    }
}

@Composable
private fun NameAndAddress(
    name: String,
    address: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = name,
            style = HeliaTheme.typography.heading4,
            color = HeliaTheme.primaryTextColor
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = R.drawable.ic_location),
                contentDescription = "",
                tint = HeliaTheme.colors.primary500
            )
            Text(
                text = address,
                style = HeliaTheme.typography.bodyMediumRegular,
                color = HeliaTheme.secondaryTextColor
            )
        }
    }
}

@Composable
private fun Gallery(
    images: List<String>,
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.hotel_details_screen_gallery_photos),
                style = HeliaTheme.typography.heading5,
                color = HeliaTheme.primaryTextColor
            )
            TextButton(
                text = stringResource(R.string.see_all_button),
                onClick = onSeeAllClick
            )
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = images, key = { it }) { url ->
                AsyncImage(
                    modifier = Modifier
                        .size(width = 140.dp, height = 100.dp)
                        .clip(HeliaTheme.shapes.large),
                    contentScale = ContentScale.Crop,
                    model = url,
                    contentDescription = "",
                )
            }
        }
    }
}

@Composable
private fun Details(
    numberOfBedrooms: Int,
    numberOfBathrooms: Int,
    squareMeters: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = stringResource(R.string.hotel_details_screen_details),
            style = HeliaTheme.typography.heading5,
            color = HeliaTheme.primaryTextColor
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LabeledIcon(
                modifier = Modifier.weight(1f),
                iconRes = R.drawable.ic_hotel,
                text = stringResource(R.string.hotel_details_screen_detail_type)
            )
            LabeledIcon(
                modifier = Modifier.weight(1f),
                iconRes = R.drawable.ic_bedroom,
                text = stringResource(
                    R.string.hotel_details_screen_detail_bedrooms,
                    numberOfBedrooms
                )
            )
            LabeledIcon(
                modifier = Modifier.weight(1f),
                iconRes = R.drawable.ic_bathroom,
                text = stringResource(
                    R.string.hotel_details_screen_detail_bathrooms,
                    numberOfBathrooms
                )
            )
            LabeledIcon(
                modifier = Modifier.weight(1f),
                iconRes = R.drawable.ic_square,
                text = stringResource(
                    R.string.hotel_details_screen_detail_square,
                    squareMeters
                )
            )
        }
    }
}

@Composable
private fun LabeledIcon(
    @DrawableRes iconRes: Int,
    text: String,
    modifier: Modifier = Modifier
) {
    val brush = Brush.linearGradient(HeliaTheme.colors.gradientGreen)
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .size(32.dp)
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawRect(brush = brush, blendMode = BlendMode.SrcAtop)
                    }
                },
            painter = painterResource(iconRes),
            contentDescription = "",
        )
        Text(
            text = text,
            style = HeliaTheme.typography.bodySmallSemiBold,
            color = HeliaTheme.secondaryTextColor,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
private fun Description(
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.hotel_details_screen_description),
            style = HeliaTheme.typography.heading5,
            color = HeliaTheme.primaryTextColor
        )
        Text(
            text = text,
            style = HeliaTheme.typography.bodyMediumRegular,
            color = HeliaTheme.secondaryTextColor
        )
    }
}

@Composable
private fun Facilities(
    facilities: List<HotelFacility>,
    modifier: Modifier = Modifier,
    column: Int = 4
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = stringResource(R.string.hotel_details_screen_facilities),
            style = HeliaTheme.typography.heading5,
            color = HeliaTheme.primaryTextColor
        )
        facilities.splitByRow().forEach { facilities ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                facilities.forEach { facility ->
                    LabeledIcon(
                        modifier = Modifier.weight(1f),
                        iconRes = facility.asDrawableRes(),
                        text = stringResource(facility.asStringRes())
                    )
                }

                // fill the last row if not full with spacers to keep a grid style
                val emptyColumns = column - facilities.size
                repeat(emptyColumns) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

private fun List<HotelFacility>.splitByRow(columns: Int = 4): List<List<HotelFacility>> {
    var mutableFacilities = this
    val result = mutableListOf<List<HotelFacility>>()

    while (mutableFacilities.isNotEmpty()) {
        val rowItems = mutableFacilities.take(columns)
        mutableFacilities = mutableFacilities.drop(columns)
        result.add(rowItems)
    }

    return result.toList()
}

@Composable
private fun Review(
    reviews: List<HotelDetails.Review>,
    numberOfReviews: Int,
    rating: Double,
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.hotel_details_screen_review),
                style = HeliaTheme.typography.heading5,
                color = HeliaTheme.primaryTextColor
            )
            ReviewsLabel(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
                numberOfReview = numberOfReviews,
                rating = rating.toString()
            )
            TextButton(
                text = stringResource(id = R.string.see_all_button),
                onClick = onSeeAllClick
            )

        }

        reviews.take(3).forEach { review ->
            ReviewCard(
                modifier = Modifier.fillMaxWidth(),
                text = review.text,
                authorName = review.author.name,
                avatarUrl = review.author.avatarUrl,
                date = review.created.toUiString(),
                rating = review.rating.toString()
            )
        }
    }
}

@Composable
private fun BookPanel(
    price: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(color = HeliaTheme.backgroundColor)
    ) {
        Divider(modifier = Modifier.fillMaxWidth())
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier.padding(end = 16.dp)) {
                Text(
                    modifier = Modifier.alignByBaseline(),
                    text = price,
                    color = HeliaTheme.colors.primary500,
                    style = HeliaTheme.typography.heading4
                )
                Text(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .alignByBaseline(),
                    text = stringResource(id = R.string.per_night),
                    style = HeliaTheme.typography.bodyMediumRegular,
                    color = HeliaTheme.secondaryTextColor
                )
            }

            PrimaryButton(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.hotel_details_screen_book_button),
                onClick = onClick
            )
        }
    }
}

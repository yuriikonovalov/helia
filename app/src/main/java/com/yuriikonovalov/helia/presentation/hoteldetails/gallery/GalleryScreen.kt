package com.yuriikonovalov.helia.presentation.hoteldetails.gallery

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.yuriikonovalov.helia.R
import com.yuriikonovalov.helia.designsystem.components.Navbar
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme

@Composable
fun GalleryScreen(
    onNavigationClick: () -> Unit,
    viewModel: GalleryScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    GalleryScreenContent(
        modifier = Modifier.fillMaxWidth(),
        state = state,
        onNavigationClick = onNavigationClick,
        onImageClick = { viewModel.handleIntent(GalleryIntent.OpenImage(it)) },
        onCloseImageClick = { viewModel.handleIntent(GalleryIntent.CloseImage) }
    )

    BackHandler(enabled = state.openImageUrl != null) {
        viewModel.handleIntent(GalleryIntent.CloseImage)
    }
}

@Composable
private fun GalleryScreenContent(
    state: GalleryUiState,
    onNavigationClick: () -> Unit,
    onImageClick: (url: String) -> Unit,
    onCloseImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {


    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        GalleryTopBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 24.dp, top = 24.dp)
                .statusBarsPadding(),
            onNavigationClick = onNavigationClick
        )

        ImageGrid(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 24.dp),
            imageUrls = state.imageUrls,
            isLoading = state.isLoading,
            onImageClick = onImageClick
        )
    }

    FullscreenImage(
        modifier = Modifier.fillMaxSize(),
        imageUrls = state.imageUrls,
        imageUrl = state.openImageUrl,
        onCloseClick = onCloseImageClick
    )
}

@Composable
private fun GalleryTopBar(
    onNavigationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Navbar(
        modifier = modifier,
        title = stringResource(R.string.gallery_screen_title),
        onNavigateClick = onNavigationClick,
        actions = {}
    )
}

@Composable
private fun ImageGrid(
    modifier: Modifier,
    isLoading: Boolean,
    imageUrls: List<String>,
    onImageClick: (url: String) -> Unit
) {
    AnimatedContent(
        modifier = modifier,
        targetState = isLoading,
        label = ""
    ) { loading ->
        if (loading) {
            LoadingContent(modifier = Modifier.fillMaxSize())
        } else {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(items = imageUrls, key = { it }) { url ->
                    AsyncImage(
                        modifier = Modifier
                            .clip(HeliaTheme.shapes.medium)
                            .aspectRatio(1.3f)
                            .clickable { onImageClick(url) },
                        contentScale = ContentScale.Crop,
                        model = url,
                        contentDescription = ""
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = HeliaTheme.colors.primary500)
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FullscreenImage(
    modifier: Modifier,
    imageUrls: List<String>,
    imageUrl: String?,
    onCloseClick: () -> Unit
) {
    if (imageUrl == null) return

    val pagerState = rememberPagerState(
        initialPage = imageUrls.indexOf(imageUrl),
        pageCount = { imageUrls.size }
    )

    Column(
        modifier = modifier.background(HeliaTheme.backgroundColor)
    ) {
        IconButton(
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 16.dp, top = 24.dp),
            onClick = onCloseClick
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_left),
                contentDescription = stringResource(id = R.string.cd_navigate_up),
                tint = if (HeliaTheme.theme.isDark) HeliaTheme.colors.white else HeliaTheme.colors.greyscale900
            )
        }

        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 24.dp),
            state = pagerState
        ) { page ->
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = imageUrls[page],
                contentScale = ContentScale.Fit,
                contentDescription = ""
            )
        }
    }
}
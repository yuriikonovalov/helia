package com.yuriikonovalov.helia.presentation.welcome.carousel

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yuriikonovalov.helia.R
import com.yuriikonovalov.helia.designsystem.components.PrimaryButton
import com.yuriikonovalov.helia.designsystem.components.SecondaryButton
import com.yuriikonovalov.helia.designsystem.components.SliderIndicator
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme


@Composable
fun CarouselScreen(
    onNavigateFurther: () -> Unit,
    viewModel: CarouselScreenViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    if (state.isOnboardingDone) {
        LaunchedEffect(Unit) {
            onNavigateFurther()
        }
    }

    CarouselScreenContent(
        modifier = Modifier.navigationBarsPadding(),
        state = state,
        onNext = { viewModel.handleIntent(CarouselIntent.Next) },
        onSkip = { viewModel.handleIntent(CarouselIntent.Skip) }
    )
}

@Composable
private fun CarouselScreenContent(
    state: CarouselUiState,
    onNext: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Image(
            modifier = Modifier.weight(1f),
            painter = painterResource(state.slide.imageResId),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.padding(vertical = 24.dp, horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(state.slide.headingResId),
                style = HeliaTheme.typography.heading4,
                textAlign = TextAlign.Center,
                color = if (HeliaTheme.theme.isDark) {
                    HeliaTheme.colors.white
                } else {
                    HeliaTheme.colors.greyscale900
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(state.slide.bodyTextResId),
                style = HeliaTheme.typography.bodyLargeRegular,
                textAlign = TextAlign.Center,
                color = if (HeliaTheme.theme.isDark) {
                    HeliaTheme.colors.greyscale400
                } else {
                    HeliaTheme.colors.greyscale700
                }
            )
            Spacer(modifier = Modifier.height(30.dp))
            SliderIndicator(
                slides = CarouselUiState.Slide.values().toList(),
                currentSlide = state.slide
            )
            Spacer(modifier = Modifier.height(30.dp))
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.carousel_button_next),
                onClick = onNext
            )
            Spacer(modifier = Modifier.height(16.dp))
            SecondaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.carousel_button_skip),
                onClick = onSkip
            )
        }
    }
}


private val CarouselUiState.Slide.imageResId: Int
    get() = when (this) {
        CarouselUiState.Slide.FIRST -> R.drawable.img_carousel_1
        CarouselUiState.Slide.SECOND -> R.drawable.img_carousel_2
        CarouselUiState.Slide.THIRD -> R.drawable.img_carousel_3
    }
private val CarouselUiState.Slide.headingResId: Int
    get() = when (this) {
        CarouselUiState.Slide.FIRST -> R.string.carousel_1_heading
        CarouselUiState.Slide.SECOND -> R.string.carousel_2_heading
        CarouselUiState.Slide.THIRD -> R.string.carousel_3_heading
    }
private val CarouselUiState.Slide.bodyTextResId: Int
    get() = when (this) {
        CarouselUiState.Slide.FIRST -> R.string.carousel_1_body_text
        CarouselUiState.Slide.SECOND -> R.string.carousel_2_body_text
        CarouselUiState.Slide.THIRD -> R.string.carousel_3_body_text
    }
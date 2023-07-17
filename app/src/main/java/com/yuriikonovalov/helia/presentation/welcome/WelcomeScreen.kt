package com.yuriikonovalov.helia.presentation.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuriikonovalov.helia.R
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onNavigateFurther: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.img_welcome),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .background(Brush.verticalGradient(HeliaTheme.colors.gradientDark))
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 24.dp, vertical = 48.dp)

            ) {
                Text(
                    text = stringResource(R.string.welcome_screen_title),
                    style = HeliaTheme.typography.heading2,
                    color = HeliaTheme.colors.white
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = HeliaTheme.typography.heading1.copy(
                        brush = Brush.linearGradient(HeliaTheme.colors.gradientGreen),
                        fontSize = 80.sp
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.welcome_screen_subtitle),
                    style = HeliaTheme.typography.bodyXLargeSemiBold,
                    color = HeliaTheme.colors.white
                )
            }

        }
    }

    LaunchedEffect(Unit) {
        delay(2_000)
        onNavigateFurther()
    }
}
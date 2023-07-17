package com.yuriikonovalov.helia.presentation.welcome.carousel

sealed interface CarouselIntent {
    object Next : CarouselIntent
    object Skip : CarouselIntent
}
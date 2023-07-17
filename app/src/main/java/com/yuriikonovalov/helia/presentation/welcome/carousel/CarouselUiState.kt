package com.yuriikonovalov.helia.presentation.welcome.carousel

data class CarouselUiState(
    val slide: Slide = Slide.FIRST,
    val isOnboardingDone: Boolean = false
) {
    fun nextSlide(): CarouselUiState {
        return when (slide) {
            Slide.FIRST -> copy(slide = Slide.SECOND)
            Slide.SECOND -> copy(slide = Slide.THIRD)
            Slide.THIRD -> this // no-op
        }
    }

    enum class Slide {
        FIRST, SECOND, THIRD
    }
}
package com.yuriikonovalov.helia.presentation.welcome.carousel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuriikonovalov.helia.domain.usecases.FinishOnboardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarouselScreenViewModel @Inject constructor(
    private val finishOnboardingUseCase: FinishOnboardingUseCase
) : ViewModel() {
    private var _uiState = MutableStateFlow(CarouselUiState())
    val uiState = _uiState.asStateFlow()

    fun handleIntent(intent: CarouselIntent) {
        when (intent) {
            CarouselIntent.Next -> handleNextIntent()
            CarouselIntent.Skip -> handleSkipIntent()
        }
    }

    private fun handleNextIntent() {
        if (uiState.value.slide == CarouselUiState.Slide.THIRD) {
            finishOnboarding()
        } else {
            _uiState.update { it.nextSlide() }
        }
    }

    private fun handleSkipIntent() = finishOnboarding()

    private fun finishOnboarding() {
        viewModelScope.launch {
            finishOnboardingUseCase()
            _uiState.update { it.copy(isOnboardingDone = true) }
        }
    }

}
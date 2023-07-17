package com.yuriikonovalov.helia.presentation.hoteldetails.review

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuriikonovalov.helia.domain.usecases.GetHotelReviewsUseCase
import com.yuriikonovalov.helia.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getHotelReviews: GetHotelReviewsUseCase
) : ViewModel() {
    private val hotelId: String = checkNotNull(savedStateHandle[ReviewNavigation.ARGUMENT])
    private val _state = MutableStateFlow(ReviewUiState())
    val state = _state.asStateFlow()

    init {
        collectHotelDetails()
    }

    fun handleIntent(intent: ReviewIntent) = when (intent) {
        is ReviewIntent.UpdateRatingFilter -> _state.update { it.updateRatingFilter(intent.ratingFilter) }
    }

    private fun collectHotelDetails() {
        viewModelScope.launch {
            _state.update { it.updateIsLoading(true) }
            val result = getHotelReviews(hotelId)
            _state.update { it.updateIsLoading(false) }
            when (result) {
                is Result.Error -> _state.update { it.updateIsError(true) }
                is Result.Success -> _state.update {
                    it.updateReviews(
                        reviews = result.data.reviews,
                        averageRating = result.data.averageRating,
                        numberOfReviews = result.data.numberOfReviews
                    )
                }
            }
        }
    }
}
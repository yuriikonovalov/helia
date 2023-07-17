package com.yuriikonovalov.helia.presentation.hoteldetails.review

import com.yuriikonovalov.helia.domain.valueobjects.RatingFilter

sealed interface ReviewIntent {
    data class UpdateRatingFilter(val ratingFilter: RatingFilter) : ReviewIntent
}
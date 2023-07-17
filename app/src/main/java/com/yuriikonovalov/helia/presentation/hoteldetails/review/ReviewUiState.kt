package com.yuriikonovalov.helia.presentation.hoteldetails.review

import com.yuriikonovalov.helia.domain.entities.HotelDetails
import com.yuriikonovalov.helia.domain.valueobjects.RatingFilter

data class ReviewUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val ratingFilter: RatingFilter = RatingFilter.ALL,
    val averageRating: Double = 0.0,
    val reviews: List<HotelDetails.Review> = emptyList(),
    val numberOfReviews: Int = 0
) {

    enum class Type {
        LOADING, ERROR, EMPTY, DATA
    }

    val type: Type
        get() = when {
            isLoading -> Type.LOADING
            isError -> Type.ERROR
            filteredReviews.isEmpty() -> Type.EMPTY
            else -> Type.DATA
        }

    val filteredReviews
        get() = reviews.filterWithRating(ratingFilter)


    fun updateIsLoading(value: Boolean) = copy(isLoading = value)

    fun updateIsError(value: Boolean) = copy(isError = value)

    fun updateRatingFilter(value: RatingFilter): ReviewUiState {
        return if (value != ratingFilter) copy(ratingFilter = value) else this
    }

    fun updateReviews(
        reviews: List<HotelDetails.Review>,
        averageRating: Double,
        numberOfReviews: Int
    ): ReviewUiState {
        return copy(
            reviews = reviews,
            averageRating = averageRating,
            numberOfReviews = numberOfReviews
        )
    }

    private fun List<HotelDetails.Review>.filterWithRating(rating: RatingFilter): List<HotelDetails.Review> {
        if (rating == RatingFilter.ALL) return this
        val intRating = when (rating) {
            RatingFilter.FIVE -> 5
            RatingFilter.FOUR -> 4
            RatingFilter.THREE -> 3
            RatingFilter.TWO -> 2
            else -> throw IllegalArgumentException("Unexpected $rating rating. RatingFilter.ALL should return the same list.")
        }
        return filter { review ->
            review.rating == intRating
        }
    }
}
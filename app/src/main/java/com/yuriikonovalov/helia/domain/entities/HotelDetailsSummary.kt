package com.yuriikonovalov.helia.domain.entities

import kotlin.math.roundToInt

data class HotelDetailsSummary(
    val details: HotelDetails,
    val isBookmarked: Boolean
) {
    val displayPrice = "$${details.hotel.price.roundToInt()}"
    val displayRating = details.hotel.rating.toString()
}

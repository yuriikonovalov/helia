package com.yuriikonovalov.helia.domain.entities

import kotlin.math.roundToInt

data class HotelSummary(
    val hotel: Hotel,
    val isBookmarked: Boolean
) {
    val displayPrice = "$${hotel.price.roundToInt()}"
    val displayRating = hotel.rating.toString()
}

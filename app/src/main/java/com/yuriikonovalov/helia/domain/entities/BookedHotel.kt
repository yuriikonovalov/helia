package com.yuriikonovalov.helia.domain.entities

import com.yuriikonovalov.helia.domain.valueobjects.BookingStatus

data class BookedHotel(
    val hotel: Hotel,
    val status: BookingStatus
) {
    val address: String = "${hotel.city}, ${hotel.country}"
}

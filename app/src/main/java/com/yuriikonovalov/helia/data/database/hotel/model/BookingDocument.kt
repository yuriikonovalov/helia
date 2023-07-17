package com.yuriikonovalov.helia.data.database.hotel.model

import com.yuriikonovalov.helia.domain.valueobjects.BookingStatus

data class BookingDocument(
    val hotelId: String = "",
    val status: BookingStatus = BookingStatus.ONGOING
//    val name: String,
//    val guests: Int,
//    val checkIn: String,
//    val checkOut: String
)

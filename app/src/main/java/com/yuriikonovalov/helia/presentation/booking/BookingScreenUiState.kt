package com.yuriikonovalov.helia.presentation.booking

import com.yuriikonovalov.helia.domain.entities.BookedHotel
import com.yuriikonovalov.helia.domain.entities.Hotel
import com.yuriikonovalov.helia.domain.valueobjects.BookingStatus

data class BookingScreenUiState(
    val isLoading: Boolean = false,
    val hotels: List<BookedHotel> = emptyList(),
    val bookingStatusFilter: BookingStatus = BookingStatus.ONGOING,
    val hotelToCancel: Hotel? = null
) {
    val hotelsToDisplay
        get() = hotels.filter { it.status == bookingStatusFilter }

    fun updateIsLoading(value: Boolean) = copy(isLoading = value)

    fun updateHotels(value: List<BookedHotel>) = copy(hotels = value)

    fun updateBookingStatusFilter(value: BookingStatus) = copy(bookingStatusFilter = value)

    fun updateHotelToCancel(value: Hotel?) = copy(hotelToCancel = value)
}

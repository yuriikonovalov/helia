package com.yuriikonovalov.helia.presentation.hoteldetails

sealed interface HotelDetailsIntent {
    data class ClickBookmark(val hotelId: String, val isBookmarked: Boolean) : HotelDetailsIntent
    data class BookHotel(val hotelId: String) : HotelDetailsIntent
}
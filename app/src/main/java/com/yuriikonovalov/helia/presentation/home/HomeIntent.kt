package com.yuriikonovalov.helia.presentation.home

import com.yuriikonovalov.helia.domain.valueobjects.HotelCategory

sealed interface HomeIntent {
    data class ChangeHotelCategory(val category: HotelCategory) : HomeIntent
    data class BookmarkHotel(val hotelId: String, val isBookmarked: Boolean) : HomeIntent
}
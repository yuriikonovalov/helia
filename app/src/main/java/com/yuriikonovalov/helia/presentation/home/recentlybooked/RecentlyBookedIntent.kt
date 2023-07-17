package com.yuriikonovalov.helia.presentation.home.recentlybooked

import com.yuriikonovalov.helia.designsystem.components.HotelDisplayMode


sealed interface RecentlyBookedIntent {
    data class ChangeViewMode(val hotelDisplayMode: HotelDisplayMode) : RecentlyBookedIntent
    data class BookmarkHotel(val hotelId: String, val isBookmarked: Boolean) : RecentlyBookedIntent
}
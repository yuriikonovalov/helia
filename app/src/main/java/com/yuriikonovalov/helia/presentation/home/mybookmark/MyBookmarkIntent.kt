package com.yuriikonovalov.helia.presentation.home.mybookmark

import com.yuriikonovalov.helia.designsystem.components.HotelDisplayMode


sealed interface MyBookmarkIntent {
    data class ChangeViewMode(val hotelDisplayMode: HotelDisplayMode) : MyBookmarkIntent
    data class DeleteBookmark(val hotelId: String) : MyBookmarkIntent
    data class OpenBottomSheet(val hotelId: String) : MyBookmarkIntent
    object HideBottomSheet : MyBookmarkIntent
}
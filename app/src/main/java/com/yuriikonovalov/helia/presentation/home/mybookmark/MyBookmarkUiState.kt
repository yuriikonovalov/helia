package com.yuriikonovalov.helia.presentation.home.mybookmark

import com.yuriikonovalov.helia.designsystem.components.HotelDisplayMode
import com.yuriikonovalov.helia.domain.entities.HotelSummary

data class MyBookmarkUiState(
    val isLoading: Boolean = true,
    val isDeleting: Boolean = false,
    val hotelDisplayMode: HotelDisplayMode = HotelDisplayMode.LIST,
    val hotels: List<HotelSummary> = emptyList(),
    val hotelIdToDelete: String? = null
) {

    val hotelToDelete: HotelSummary?
        get() = hotels.find { it.hotel.id == hotelIdToDelete }

    fun updateIsLoading(value: Boolean) = copy(isLoading = value)

    fun updateIsDeleting(value: Boolean) = copy(isDeleting = value)

    fun updateDisplayMode(value: HotelDisplayMode) = copy(hotelDisplayMode = value)

    fun updateHotels(value: List<HotelSummary>) = copy(hotels = value)

    fun updateHotelIdToDelete(hotelId: String?) = copy(hotelIdToDelete = hotelId)
}

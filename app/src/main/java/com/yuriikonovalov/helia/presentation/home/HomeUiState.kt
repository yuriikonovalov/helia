package com.yuriikonovalov.helia.presentation.home

import androidx.compose.runtime.Immutable
import com.yuriikonovalov.helia.domain.entities.HotelSummary
import com.yuriikonovalov.helia.domain.valueobjects.HotelCategory


@Immutable
data class HomeUiState(
    val userName: String? = null,
    val hotelCategory: HotelCategory = HotelCategory.RECOMMENDED,
    val hotels: List<HotelSummary> = emptyList(),
    val recentlyBookedHotels: List<HotelSummary> = emptyList(),
    val isHotelByCategoryLoading: Boolean = false,
) {
    fun updateIsHotelByCategoryLoading(value: Boolean) = copy(isHotelByCategoryLoading = value)
    fun updateUserName(value: String?) = copy(userName = value)
    fun updateHotelCategory(value: HotelCategory) = copy(hotelCategory = value)
    fun updateHotels(value: List<HotelSummary>) = copy(hotels = value)
    fun updateRecentlyBookedHotels(value: List<HotelSummary>) = copy(recentlyBookedHotels = value)
}

package com.yuriikonovalov.helia.domain.valueobjects

import androidx.compose.runtime.Immutable

@Immutable
data class StatefulHotelSearchFilter(
    private val current: HotelSearchFilter = HotelSearchFilter(),
    private val snapshot: HotelSearchFilter = HotelSearchFilter()
) {
    val value get() = current

    fun resetFilter() = copy(
        snapshot = HotelSearchFilter(),
        current = HotelSearchFilter()
    )

    fun resetCurrentFilter() = copy(current = snapshot)

    fun applyFilter() = copy(snapshot = current)

    fun updateCountry(country: String) = copy(current = current.updateCountry(country))

    fun updateFacility(facility: HotelFacility) = copy(current = current.updateFacilities(facility))

    fun updateSort(sort: HotelSearchFilter.HotelSearchSort) = copy(
        current = current.updateSort(sort)
    )

    fun updatePriceRange(range: HotelSearchFilter.PriceRange) = copy(
        current = current.updatePriceRange(range)
    )
}
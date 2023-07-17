package com.yuriikonovalov.helia.presentation.search

import androidx.compose.runtime.Immutable
import com.yuriikonovalov.helia.designsystem.components.HotelDisplayMode
import com.yuriikonovalov.helia.domain.entities.HotelSummary
import com.yuriikonovalov.helia.domain.entities.SearchQuery
import com.yuriikonovalov.helia.domain.valueobjects.HotelCategory
import com.yuriikonovalov.helia.domain.valueobjects.HotelFacility
import com.yuriikonovalov.helia.domain.valueobjects.HotelSearchFilter
import com.yuriikonovalov.helia.domain.valueobjects.StatefulHotelSearchFilter

@Immutable
data class SearchUiState(
    val searchValue: String = "",
    val isSearching: Boolean = false,
    val hotelCategory: HotelCategory? = null,
    val displayMode: HotelDisplayMode = HotelDisplayMode.LIST,
    val isSearchBarFocused: Boolean = false,
    val searchQueries: List<SearchQuery> = emptyList(),
    val statefulFilter: StatefulHotelSearchFilter = StatefulHotelSearchFilter(),
    val hotels: List<HotelSummary> = emptyList(),
    val countries: List<String> = emptyList()
) {

    val filter: HotelSearchFilter
        get() = statefulFilter.value

    val numberOfHotels: Int
        get() = hotels.size


    fun updateIsSearching(value: Boolean) = copy(isSearching = value)

    fun updateSearchValue(value: String) = copy(searchValue = value)

    fun updateHotelCategory(value: HotelCategory?) = copy(hotelCategory = value)

    fun updateDisplayMode(value: HotelDisplayMode) = copy(displayMode = value)

    fun updateIsSearchBarFocused(value: Boolean) = copy(isSearchBarFocused = value)

    fun updateSearchQueries(value: List<SearchQuery>) = copy(searchQueries = value)


    fun updateHotels(value: List<HotelSummary>) = copy(hotels = value)

    fun updateCountries(value: List<String>) = copy(countries = value)

    fun updateCountryFilter(value: String) = copy(
        statefulFilter = statefulFilter.updateCountry(value)
    )

    fun updateFacilityFilter(value: HotelFacility) = copy(
        statefulFilter = statefulFilter.updateFacility(value)
    )

    fun updateSort(value: HotelSearchFilter.HotelSearchSort) = copy(
        statefulFilter = statefulFilter.updateSort(value)
    )

    fun resetFilter() = copy(statefulFilter = statefulFilter.resetFilter())

    fun resetUnappliedFilter() = copy(
        statefulFilter = statefulFilter.resetCurrentFilter()
    )

    fun applyFilter() = copy(statefulFilter = statefulFilter.applyFilter())
    fun updatePriceFilter(value: HotelSearchFilter.PriceRange) = copy(
        statefulFilter = statefulFilter.updatePriceRange(value)
    )

}

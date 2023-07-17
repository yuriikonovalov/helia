package com.yuriikonovalov.helia.domain.valueobjects

import androidx.compose.runtime.Immutable

@Immutable
data class HotelSearchFilter(
    val countries: List<String> = emptyList(),
    val sort: HotelSearchSort? = null,
    val priceRange: PriceRange = PriceRange(),
    val facilities: List<HotelFacility> = emptyList()
) {

    fun updateCountry(country: String): HotelSearchFilter {
        return copy(countries = countries.addOrRemove(country))
    }

    fun updateFacilities(facility: HotelFacility): HotelSearchFilter {
        return copy(facilities = facilities.addOrRemove(facility))
    }

    fun updateSort(sort: HotelSearchSort): HotelSearchFilter {
        return if (this.sort == sort) {
            copy(sort = null)
        } else {
            copy(sort = sort)
        }
    }

    private fun <T> List<T>.addOrRemove(element: T): List<T> {
        return if (this.contains(element)) {
            this.filterNot { it == element }
        } else {
            this + element
        }
    }

    fun updatePriceRange(range: PriceRange): HotelSearchFilter {
        return copy(priceRange = range)
    }

    data class PriceRange(
        val from: Double = DEFAULT_FROM,
        val to: Double = DEFAULT_TO
    ) {
        companion object {
            const val DEFAULT_FROM = 0.0
            // const val DEFAULT_TO = Double.MAX_VALUE
            const val DEFAULT_TO = 500.0
        }
    }

    enum class HotelSearchSort {
        REVIEW_SCORE, HIGHEST_PRICE, LOWEST_PRICE
    }
}

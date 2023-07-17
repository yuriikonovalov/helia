package com.yuriikonovalov.helia.presentation.utils

import com.yuriikonovalov.helia.R
import com.yuriikonovalov.helia.domain.valueobjects.HotelSearchFilter
import com.yuriikonovalov.helia.domain.valueobjects.HotelSearchFilter.HotelSearchSort.*

fun HotelSearchFilter.HotelSearchSort.asStringRes() = when (this) {
    REVIEW_SCORE -> R.string.search_screen_sort_option_review_score
    HIGHEST_PRICE -> R.string.search_screen_sort_option_highest_price
    LOWEST_PRICE -> R.string.search_screen_sort_option_lowest_price
}
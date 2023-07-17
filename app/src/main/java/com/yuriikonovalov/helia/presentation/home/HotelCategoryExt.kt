package com.yuriikonovalov.helia.presentation.home

import com.yuriikonovalov.helia.R
import com.yuriikonovalov.helia.domain.valueobjects.HotelCategory

val HotelCategory.stringResId
    get() = when (this) {
        HotelCategory.POPULAR -> R.string.hotel_category_popular
        HotelCategory.RECOMMENDED -> R.string.hotel_category_recommended
        HotelCategory.TRENDING -> R.string.hotel_category_trending
    }
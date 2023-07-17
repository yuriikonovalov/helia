package com.yuriikonovalov.helia.presentation.utils

import com.yuriikonovalov.helia.R
import com.yuriikonovalov.helia.domain.valueobjects.HotelFacility
import com.yuriikonovalov.helia.domain.valueobjects.HotelFacility.*

fun HotelFacility.asStringRes() = when (this) {
    SWIMMING_POOL -> R.string.search_screen_filter_facility_swimming_pool
    WI_FI -> R.string.search_screen_filter_facility_wi_fi
    RESTAURANT -> R.string.search_screen_filter_facility_restaurant
    PARKING -> R.string.search_screen_filter_facility_parking
    MEETING_ROOM -> R.string.search_screen_filter_facility_meeting_room
    ELEVATOR -> R.string.search_screen_filter_facility_elevator
    FITNESS_CENTER -> R.string.search_screen_filter_facility_fitness_center
    DAY_AND_NIGHT_OPEN -> R.string.search_screen_filter_facility_day_and_night_open
}

fun HotelFacility.asDrawableRes() = when (this) {
    SWIMMING_POOL -> R.drawable.ic_swimming_pool
    WI_FI -> R.drawable.ic_wi_fi
    RESTAURANT -> R.drawable.ic_restaurant
    PARKING -> R.drawable.ic_parking
    MEETING_ROOM -> R.drawable.ic_meeting_room
    ELEVATOR -> R.drawable.ic_elevator
    FITNESS_CENTER -> R.drawable.ic_fitness_center
    DAY_AND_NIGHT_OPEN -> R.drawable.ic_24_7
}
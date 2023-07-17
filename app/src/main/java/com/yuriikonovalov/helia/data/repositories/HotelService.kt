package com.yuriikonovalov.helia.data.repositories

import com.yuriikonovalov.helia.domain.entities.Hotel
import com.yuriikonovalov.helia.domain.entities.HotelDetails
import com.yuriikonovalov.helia.domain.valueobjects.HotelCategory
import com.yuriikonovalov.helia.domain.valueobjects.HotelSearchFilter

interface HotelService {
    suspend fun searchHotels(categories: List<HotelCategory>): List<Hotel>
    suspend fun getHotelsByIds(ids: List<String>): List<Hotel>
    suspend fun getHotelDetails(hotelId: String): HotelDetails
    suspend fun searchHotelsWithFilter(
        query: String,
        categories: List<HotelCategory>,
        filter: HotelSearchFilter
    ): List<Hotel>

    suspend fun getCountries(): List<String>
}
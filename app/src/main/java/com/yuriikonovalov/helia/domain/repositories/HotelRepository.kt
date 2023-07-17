package com.yuriikonovalov.helia.domain.repositories

import com.yuriikonovalov.helia.domain.entities.BookedHotel
import com.yuriikonovalov.helia.domain.entities.Hotel
import com.yuriikonovalov.helia.domain.entities.HotelDetails
import com.yuriikonovalov.helia.domain.valueobjects.BookingStatus
import com.yuriikonovalov.helia.domain.valueobjects.HotelCategory
import com.yuriikonovalov.helia.domain.valueobjects.HotelSearchFilter
import kotlinx.coroutines.flow.Flow

interface HotelRepository {
    suspend fun getHotelsByIds(ids: List<String>): List<Hotel>
    suspend fun getHotelsByCategories(
        categories: List<HotelCategory>,
        limit: Int = Int.MAX_VALUE
    ): List<Hotel>

    suspend fun searchHotelsWithFilter(
        query: String,
        categories: List<HotelCategory>,
        filter: HotelSearchFilter
    ): List<Hotel>


    fun getBookedHotels(): Flow<List<BookedHotel>>
    suspend fun addBookmark(hotelId: String)
    suspend fun deleteBookmark(hotelId: String)
    suspend fun addToBooked(hotelId: String)
    suspend fun deleteFromBooked(hotelId: String)
    fun observeBookmarkedHotelIds(): Flow<List<String>>
    suspend fun getCountries(): List<String>
    suspend fun getHotelDetails(hotelId: String): HotelDetails
    suspend fun getHotelImages(hotelId: String): List<String>
    suspend fun updateBookingStatus(hotelId: String, status: BookingStatus)
}
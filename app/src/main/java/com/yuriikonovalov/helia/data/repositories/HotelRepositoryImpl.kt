package com.yuriikonovalov.helia.data.repositories

import com.yuriikonovalov.helia.data.database.hotel.HotelDatabaseService
import com.yuriikonovalov.helia.data.database.hotel.model.BookingDocument
import com.yuriikonovalov.helia.domain.entities.BookedHotel
import com.yuriikonovalov.helia.domain.entities.Hotel
import com.yuriikonovalov.helia.domain.entities.HotelDetails
import com.yuriikonovalov.helia.domain.repositories.HotelRepository
import com.yuriikonovalov.helia.domain.repositories.UserRepository
import com.yuriikonovalov.helia.domain.valueobjects.BookingStatus
import com.yuriikonovalov.helia.domain.valueobjects.HotelCategory
import com.yuriikonovalov.helia.domain.valueobjects.HotelSearchFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class HotelRepositoryImpl @Inject constructor(
    private val hotelService: HotelService,
    private val hotelDatabase: HotelDatabaseService,
    private val userRepository: UserRepository
) : HotelRepository {

    private val uid: String
        get() = userRepository.currentUser!!.uid

    override suspend fun getHotelsByIds(ids: List<String>): List<Hotel> {
        return hotelService.getHotelsByIds(ids)
    }

    override suspend fun getHotelsByCategories(
        categories: List<HotelCategory>,
        limit: Int
    ): List<Hotel> {
        return hotelService.searchHotels(categories).take(limit)
    }

    override suspend fun searchHotelsWithFilter(
        query: String,
        categories: List<HotelCategory>,
        filter: HotelSearchFilter
    ): List<Hotel> {
        return hotelService.searchHotelsWithFilter(query, categories, filter)
    }

    override fun getBookedHotels(): Flow<List<BookedHotel>> {
        return hotelDatabase
            .getBookingDocuments(uid)
            .mapLatest { docs ->
                val ids = docs.map { it.hotelId }
                val hotels = hotelService.getHotelsByIds(ids)
                hotels.map { hotel ->
                    BookedHotel(
                        hotel = hotel,
                        status = docs.find { it.hotelId == hotel.id }!!.status
                    )
                }
            }
    }

    override suspend fun addBookmark(hotelId: String) {
        hotelDatabase.addBookmark(uid = uid, hotelId = hotelId)
    }

    override suspend fun deleteBookmark(hotelId: String) {
        hotelDatabase.deleteBookmark(uid = uid, hotelId = hotelId)
    }

    override suspend fun addToBooked(hotelId: String) {
        val bookingDocument = BookingDocument(
            hotelId = hotelId,
            status = BookingStatus.ONGOING
        )
        hotelDatabase.addToBooked(uid, bookingDocument)
    }

    override suspend fun deleteFromBooked(hotelId: String) {

    }

    override fun observeBookmarkedHotelIds(): Flow<List<String>> {
        return hotelDatabase.observeBookmarkedHotelIds(uid = uid)
    }

    override suspend fun getCountries(): List<String> {
        return hotelService.getCountries()
    }

    override suspend fun getHotelDetails(hotelId: String): HotelDetails {
        return hotelService.getHotelDetails(hotelId)
    }

    override suspend fun getHotelImages(hotelId: String): List<String> {
        return hotelService.getHotelDetails(hotelId).photoUrls
    }

    override suspend fun updateBookingStatus(hotelId: String, status: BookingStatus) {
        hotelDatabase.updateBookedHotelStatus(
            uid = uid,
            hotelId = hotelId,
            status = status
        )
    }
}
package com.yuriikonovalov.helia.domain.entities

import com.yuriikonovalov.helia.domain.valueobjects.HotelFacility
import java.time.LocalDate

data class HotelDetails(
    val hotel: Hotel,
    val address: Address,
    val description: String,
    val photoUrls: List<String>,
    val hotelInformation: HotelInformation,
    val facilities: List<HotelFacility>,
    val location: Location,
    val reviews: List<Review>
) {
    data class Address(
        val country: String,
        val city: String,
        val addressLine1: String
    )

    data class HotelInformation(
        val numberOfBedrooms: Int,
        val numberOfBathrooms: Int,
        val squareMeters: Int
    )

    data class Location(
        val longitude: String,
        val latitude: String
    )

    data class Review(
        val author: Author,
        val text: String,
        val rating: Int,
        val created: LocalDate
    ) {
        data class Author(
            val name: String,
            val avatarUrl: String
        )
    }
}

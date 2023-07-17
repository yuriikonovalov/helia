package com.yuriikonovalov.helia.data.api.model

import android.icu.math.BigDecimal
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.yuriikonovalov.helia.domain.valueobjects.HotelCategory
import com.yuriikonovalov.helia.domain.valueobjects.HotelFacility
import java.time.LocalDate

@JsonClass(generateAdapter = true)
data class HotelProfile(
    val id: String,
    val name: String,
    val price: Double,
    val address: Address,
    val description: String,
    val category: HotelCategory,
    @Json(name = "image_urls")
    val imageUrls: List<String>,
    @Json(name = "hotel_information")
    val hotelInformation: HotelInformation,
    val facilities: List<HotelFacility>,
    val location: Location,
    val reviews: List<Review>
) {
    val numberOfReviews: Int
        get() = reviews.count()

    val rating: Double
        get() = reviews
            .map { it.rating }
            .average()
            .reduceDecimals()

    private fun Double.reduceDecimals() = BigDecimal(this)
        .setScale(1, BigDecimal.ROUND_HALF_EVEN)
        .toDouble()


    @JsonClass(generateAdapter = true)
    data class Address(
        val country: String,
        val city: String,
        @Json(name = "address_line_1")
        val addressLine1: String
    )

    @JsonClass(generateAdapter = true)
    data class HotelInformation(
        @Json(name = "number_of_bedrooms")
        val numberOfBedrooms: Int,
        @Json(name = "number_of_bathrooms")
        val numberOfBathrooms: Int,
        @Json(name = "square_meters")
        val squareMeters: Int
    )

    @JsonClass(generateAdapter = true)
    data class Location(
        val longitude: String,
        val latitude: String
    )

    @JsonClass(generateAdapter = true)
    data class Review(
        val author: Author,
        val text: String,
        val rating: Int,
        val created: LocalDate
    ) {
        @JsonClass(generateAdapter = true)
        data class Author(
            val name: String,
            @Json(name = "avatar_url")
            val avatarUrl: String
        )
    }
}

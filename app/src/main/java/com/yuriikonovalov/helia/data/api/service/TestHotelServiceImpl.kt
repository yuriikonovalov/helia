package com.yuriikonovalov.helia.data.api.service

import android.content.Context
import com.squareup.moshi.Moshi
import com.yuriikonovalov.helia.data.api.model.HotelProfile
import com.yuriikonovalov.helia.data.repositories.HotelService
import com.yuriikonovalov.helia.domain.entities.Hotel
import com.yuriikonovalov.helia.domain.entities.HotelDetails
import com.yuriikonovalov.helia.domain.valueobjects.HotelCategory
import com.yuriikonovalov.helia.domain.valueobjects.HotelFacility
import com.yuriikonovalov.helia.domain.valueobjects.HotelSearchFilter
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.random.Random

class TestHotelServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : HotelService {
    private val moshi by lazy {
        Moshi.Builder()
            .add(LocalDateAdapter())
            .build()
    }

    private val hotels: List<HotelProfile> by lazy {
        getAllHotelProfiles().shuffled(Random.Default)
    }


    override suspend fun searchHotels(categories: List<HotelCategory>): List<Hotel> {
        delay(Random.nextLong(from = 0, until = 2000))

        return hotels
            .filter { it.category in categories }
            .map { it.toHotel() }
    }

    override suspend fun searchHotelsWithFilter(
        query: String,
        categories: List<HotelCategory>,
        filter: HotelSearchFilter
    ): List<Hotel> {
        delay(Random.nextLong(from = 0, until = 2000))

        return hotels
            .asSequence()
            .filterCategory(categories)
            .filterCountries(filter.countries)
            .filterPriceRange(filter.priceRange)
            .filterFacilities(filter.facilities)
            .filterName(query)
            .sortedWith(filter.sort)
            .map { it.toHotel() }
            .toList()
    }

    override suspend fun getCountries(): List<String> {
        return readJson("countries.json", CountriesResponse::class.java).data
    }

    override suspend fun getHotelsByIds(ids: List<String>): List<Hotel> {
        delay(Random.nextLong(from = 0, until = 2000))
        return hotels
            .filter { it.id in ids }
            .map { it.toHotel() }
    }

    override suspend fun getHotelDetails(hotelId: String): HotelDetails {
        delay(Random.nextLong(from = 0, until = 2000))

        val hotelProfile = hotels.find { hotelProfile -> hotelProfile.id == hotelId }
        checkNotNull(hotelProfile)
        return HotelDetails(
            hotel = Hotel(
                id = hotelProfile.id,
                imageUrl = hotelProfile.imageUrls.first(),
                name = hotelProfile.name,
                country = hotelProfile.address.country,
                city = hotelProfile.address.city,
                rating = hotelProfile.rating,
                numberOfReviews = hotelProfile.numberOfReviews,
                price = hotelProfile.price
            ),
            address = HotelDetails.Address(
                country = hotelProfile.address.country,
                city = hotelProfile.address.city,
                addressLine1 = hotelProfile.address.addressLine1
            ),
            description = hotelProfile.description,
            photoUrls = hotelProfile.imageUrls,
            hotelInformation = HotelDetails.HotelInformation(
                numberOfBedrooms = hotelProfile.hotelInformation.numberOfBedrooms,
                numberOfBathrooms = hotelProfile.hotelInformation.numberOfBathrooms,
                squareMeters = hotelProfile.hotelInformation.squareMeters
            ),
            facilities = hotelProfile.facilities,
            location = HotelDetails.Location(
                longitude = hotelProfile.location.longitude,
                latitude = hotelProfile.location.latitude
            ),
            reviews = hotelProfile.reviews.toDomain()
        )
    }

    private fun List<HotelProfile.Review>.toDomain() = map { review ->
        HotelDetails.Review(
            author = HotelDetails.Review.Author(
                name = review.author.name,
                avatarUrl = review.author.avatarUrl,
            ),
            text = review.text,
            rating = review.rating,
            created = review.created
        )
    }


    private fun getAllHotelProfiles(): List<HotelProfile> {
        return readJson("hotels.json", HotelsResponse::class.java).data
    }

    private fun <T> readJson(fileName: String, type: Class<T>): T {
        val jsonString = context.assets.open(fileName).use { inputStream ->
            inputStream.reader().use { inputStreamReader ->
                inputStreamReader.readText()
            }
        }

        val adapter = moshi.adapter(type)
        return adapter.fromJson(jsonString)!!
    }


    private fun HotelProfile.toHotel() = Hotel(
        id = this.id,
        imageUrl = this.imageUrls.first(),
        name = this.name,
        country = this.address.country,
        city = this.address.city,
        rating = this.rating,
        numberOfReviews = this.numberOfReviews,
        price = this.price
    )

    private fun Sequence<HotelProfile>.filterName(query: String): Sequence<HotelProfile> {
        return this.filter { hotelProfile -> hotelProfile.name.contains(query, ignoreCase = true) }
    }

    private fun Sequence<HotelProfile>.filterCategory(categories: List<HotelCategory>): Sequence<HotelProfile> {
        return this.filter { hotelProfile -> hotelProfile.category in categories }
    }


    private fun Sequence<HotelProfile>.filterCountries(countries: List<String>): Sequence<HotelProfile> {
        return if (countries.isNotEmpty()) {
            this.filter { hotelProfile -> hotelProfile.address.country in countries }
        } else {
            this
        }
    }

    private fun Sequence<HotelProfile>.filterFacilities(facilities: List<HotelFacility>): Sequence<HotelProfile> {
        return if (facilities.isNotEmpty()) {
            this.filter { hotelProfile -> hotelProfile.facilities.containsAll(facilities) }
        } else {
            this
        }
    }

    private fun Sequence<HotelProfile>.filterPriceRange(priceRange: HotelSearchFilter.PriceRange): Sequence<HotelProfile> {
        val fromLimit = HotelSearchFilter.PriceRange.DEFAULT_FROM
        val toLimit = HotelSearchFilter.PriceRange.DEFAULT_TO
        return when {
            priceRange.from > fromLimit && priceRange.to < toLimit -> this.filter { hotelProfile ->
                hotelProfile.price >= priceRange.from && hotelProfile.price <= priceRange.to
            }

            priceRange.from == fromLimit && priceRange.to < toLimit -> this.filter { hotelProfile ->
                hotelProfile.price <= priceRange.to
            }

            priceRange.from > fromLimit && priceRange.to == toLimit -> this.filter { hotelProfile ->
                hotelProfile.price >= priceRange.from
            }

            else -> this
        }
    }


    private fun Sequence<HotelProfile>.sortedWith(sort: HotelSearchFilter.HotelSearchSort?): Sequence<HotelProfile> {
        return when (sort) {
            HotelSearchFilter.HotelSearchSort.REVIEW_SCORE -> sortedByDescending { hotelProfile ->
                hotelProfile.reviews
                    .map { review -> review.rating }
                    .average()
            }

            HotelSearchFilter.HotelSearchSort.HIGHEST_PRICE -> sortedByDescending { hotelProfile ->
                hotelProfile.price
            }

            HotelSearchFilter.HotelSearchSort.LOWEST_PRICE -> sortedBy { hotelProfile ->
                hotelProfile.price
            }

            null -> this
        }
    }
}




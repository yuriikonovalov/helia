package com.yuriikonovalov.helia.domain.usecases

import com.yuriikonovalov.helia.domain.entities.HotelSummary
import com.yuriikonovalov.helia.domain.repositories.HotelRepository
import com.yuriikonovalov.helia.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

interface GetRecentlyBookedHotelsUseCase {
    suspend operator fun invoke(limited: Boolean): Flow<Result<List<HotelSummary>>>
}

class GetRecentlyBookedHotelsUseCaseImpl @Inject constructor(
    private val hotelRepository: HotelRepository
) : GetRecentlyBookedHotelsUseCase {
    override suspend fun invoke(limited: Boolean): Flow<Result<List<HotelSummary>>> {
        return try {
            val bookedHotelIds = listOf(
                "b4376320-a671-4752-9cf1-35be0a7b6a48",
                "fa8982e3-5c62-4df0-95ad-cc465919937a",
                "f3fb59fb-6940-4390-8e77-505757bfcbca",
                "eee4a28b-28c7-47d3-a62e-6e9c2b1f66e8",
                "560bca39-f9b9-468c-a7dc-5e63ad6dee9b",
                "36cdb039-62e9-41bd-bd08-9130198c3c3d",
                "4795b64c-35fd-44b2-913b-af15422fbb4f",
            )

            var hotels = hotelRepository.getHotelsByIds(bookedHotelIds)
            if (limited) hotels = hotels.take(LIMITED_COUNT)

            val bookmarksFlow = hotelRepository.observeBookmarkedHotelIds()
            combine(bookmarksFlow, flowOf(hotels)) { bookmarks, _ ->
                val hotelSummaries = hotels.map { hotel ->
                    HotelSummary(
                        hotel = hotel,
                        isBookmarked = hotel.id in bookmarks
                    )
                }
                Result.Success(hotelSummaries)
            }
        } catch (exception: Exception) {
            flowOf(Result.Error(exception))
        }
    }


    companion object {
        private const val LIMITED_COUNT = 5
    }
}
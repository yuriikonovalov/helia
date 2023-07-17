package com.yuriikonovalov.helia.domain.usecases

import com.yuriikonovalov.helia.domain.entities.HotelSummary
import com.yuriikonovalov.helia.domain.repositories.HotelRepository
import com.yuriikonovalov.helia.domain.valueobjects.HotelCategory
import com.yuriikonovalov.helia.domain.valueobjects.HotelSearchFilter
import com.yuriikonovalov.helia.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

interface SearchHotelsUseCase {
    suspend operator fun invoke(
        query: String,
        categories: List<HotelCategory>,
        filter: HotelSearchFilter
    ): Flow<Result<List<HotelSummary>>>
}

class SearchHotelsUseCaseImpl @Inject constructor(
    private val hotelRepository: HotelRepository
) : SearchHotelsUseCase {
    override suspend fun invoke(
        query: String,
        categories: List<HotelCategory>,
        filter: HotelSearchFilter
    ): Flow<Result<List<HotelSummary>>> {
        return try {
            val hotelsFlow = flowOf(
                hotelRepository.searchHotelsWithFilter(
                    query = query,
                    categories = categories,
                    filter = filter
                )
            )
            val bookmarkedHotelIdsFlow = hotelRepository.observeBookmarkedHotelIds()

            combine(hotelsFlow, bookmarkedHotelIdsFlow) { hotels, bookmarkedHotelIds ->
                val hotelSummaries = hotels.map { hotel ->
                    HotelSummary(hotel = hotel, isBookmarked = hotel.id in bookmarkedHotelIds)
                }
                Result.Success(hotelSummaries)
            }
        } catch (exception: Exception) {
            flowOf(Result.Error(exception))
        }
    }
}
package com.yuriikonovalov.helia.domain.usecases

import com.yuriikonovalov.helia.domain.entities.HotelSummary
import com.yuriikonovalov.helia.domain.repositories.HotelRepository
import com.yuriikonovalov.helia.domain.valueobjects.HotelCategory
import com.yuriikonovalov.helia.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

interface GetHotelsByCategoryUseCase {
    suspend operator fun invoke(category: HotelCategory): Flow<Result<List<HotelSummary>>>
}


class GetHotelsByCategoryUseCaseImpl @Inject constructor(
    private val hotelRepository: HotelRepository
) : GetHotelsByCategoryUseCase {
    override suspend fun invoke(category: HotelCategory): Flow<Result<List<HotelSummary>>> {
        return try {
            val bookmarkedFlow = hotelRepository.observeBookmarkedHotelIds()
            val hotelsFlow = flowOf(hotelRepository.getHotelsByCategories(listOf(category), 5))

            combine(hotelsFlow, bookmarkedFlow) { hotels, bookmarkedHotelIds ->
                val hotelSummaries = hotels.map { hotel ->
                    HotelSummary(
                        hotel = hotel,
                        isBookmarked = hotel.id in bookmarkedHotelIds
                    )
                }

                Result.Success(hotelSummaries)
            }
        } catch (exception: Exception) {
            flowOf(Result.Error(exception))
        }
    }
}
package com.yuriikonovalov.helia.domain.usecases

import com.yuriikonovalov.helia.domain.entities.HotelDetailsSummary
import com.yuriikonovalov.helia.domain.repositories.HotelRepository
import com.yuriikonovalov.helia.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

interface GetHotelDetailsUseCase {
    suspend operator fun invoke(id: String): Flow<Result<HotelDetailsSummary>>
}

class GetHotelDetailsUseCaseImpl @Inject constructor(
    private val hotelRepository: HotelRepository
) : GetHotelDetailsUseCase {
    override suspend fun invoke(id: String): Flow<Result<HotelDetailsSummary>> {
        return try {
            val bookmarkedFlow = hotelRepository.observeBookmarkedHotelIds()
            val hotelDetailsFlow = flowOf(hotelRepository.getHotelDetails(id))

            combine(hotelDetailsFlow, bookmarkedFlow) { details, bookmarkedHotelIds ->
                val hotelDetailsSummary = HotelDetailsSummary(
                    details = details,
                    isBookmarked = details.hotel.id in bookmarkedHotelIds
                )
                Result.Success(hotelDetailsSummary)
            }
        } catch (exception: Exception) {
            flowOf(Result.Error(exception))
        }
    }
}
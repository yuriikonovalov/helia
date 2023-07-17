package com.yuriikonovalov.helia.domain.usecases

import com.yuriikonovalov.helia.domain.entities.HotelSummary
import com.yuriikonovalov.helia.domain.repositories.HotelRepository
import com.yuriikonovalov.helia.utils.Result
import com.yuriikonovalov.helia.utils.asResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface GetBookmarkedHotelsUseCase {
    operator fun invoke(): Flow<Result<List<HotelSummary>>>
}

class GetBookmarkedHotelsUseCaseImpl @Inject constructor(
    private val hotelRepository: HotelRepository
) : GetBookmarkedHotelsUseCase {
    override fun invoke(): Flow<Result<List<HotelSummary>>> {
        return hotelRepository
            .observeBookmarkedHotelIds()
            .map { hotelIds -> hotelRepository.getHotelsByIds(hotelIds) }
            .map { hotels ->
                hotels.map { hotel -> HotelSummary(hotel = hotel, isBookmarked = true) }
            }
            .asResult()
    }

}
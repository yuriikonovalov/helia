package com.yuriikonovalov.helia.domain.usecases

import com.yuriikonovalov.helia.domain.entities.BookedHotel
import com.yuriikonovalov.helia.domain.repositories.HotelRepository
import com.yuriikonovalov.helia.utils.Result
import com.yuriikonovalov.helia.utils.asResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetBookedHotelsUseCase {
    operator fun invoke(): Flow<Result<List<BookedHotel>>>
}

class GetBookedHotelsUseCaseImpl @Inject constructor(
    private val hotelRepository: HotelRepository
) : GetBookedHotelsUseCase {
    override fun invoke(): Flow<Result<List<BookedHotel>>> {
        return hotelRepository.getBookedHotels().asResult()
    }
}
package com.yuriikonovalov.helia.domain.usecases

import com.yuriikonovalov.helia.domain.repositories.HotelRepository
import com.yuriikonovalov.helia.domain.valueobjects.BookingStatus
import com.yuriikonovalov.helia.utils.Result
import javax.inject.Inject

interface CancelBookingUseCase {
    suspend operator fun invoke(hotelId: String): Result<Unit>
}

class CancelBookingUseCaseImpl @Inject constructor(
    private val hotelRepository: HotelRepository
) : CancelBookingUseCase {
    override suspend fun invoke(hotelId: String): Result<Unit> {
        return try {
            hotelRepository.updateBookingStatus(hotelId, BookingStatus.CANCELED)
            Result.Success(Unit)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }

}
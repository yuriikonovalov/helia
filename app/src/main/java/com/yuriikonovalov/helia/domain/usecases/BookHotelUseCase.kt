package com.yuriikonovalov.helia.domain.usecases

import com.yuriikonovalov.helia.domain.repositories.HotelRepository
import com.yuriikonovalov.helia.utils.Result
import javax.inject.Inject

interface BookHotelUseCase {
    suspend operator fun invoke(hotelId: String): Result<Unit>
}

class BookHotelUseCaseImpl @Inject constructor(
    private val hotelRepository: HotelRepository
) : BookHotelUseCase {
    override suspend fun invoke(hotelId: String): Result<Unit> {
        return try {
            hotelRepository.addToBooked(hotelId)
            Result.Success(Unit)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
}
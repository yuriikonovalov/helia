package com.yuriikonovalov.helia.domain.usecases

import com.yuriikonovalov.helia.domain.repositories.HotelRepository
import com.yuriikonovalov.helia.utils.Result
import javax.inject.Inject

interface GetHotelImagesUseCase {
    suspend operator fun invoke(hotelId: String): Result<List<String>>
}

class GetHotelImagesUseCaseImpl @Inject constructor(
    private val hotelRepository: HotelRepository
) : GetHotelImagesUseCase {
    override suspend fun invoke(hotelId: String): Result<List<String>> {
        return try {
            val imageUrls = hotelRepository.getHotelImages(hotelId)
            Result.Success(imageUrls)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
}
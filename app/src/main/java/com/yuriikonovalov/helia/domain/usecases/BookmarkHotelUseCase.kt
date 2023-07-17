package com.yuriikonovalov.helia.domain.usecases

import com.yuriikonovalov.helia.domain.repositories.HotelRepository
import com.yuriikonovalov.helia.utils.Result
import javax.inject.Inject

interface BookmarkHotelUseCase {
    suspend operator fun invoke(hotelId: String, bookmark: Boolean): Result<Unit>
}

class BookmarkHotelUseCaseImpl @Inject constructor(
    private val hotelRepository: HotelRepository
) : BookmarkHotelUseCase {
    override suspend fun invoke(hotelId: String, bookmark: Boolean): Result<Unit> {
        return try {
            if (bookmark) {
                hotelRepository.addBookmark(hotelId)
            } else {
                hotelRepository.deleteBookmark(hotelId)
            }
            Result.Success(Unit)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }

}
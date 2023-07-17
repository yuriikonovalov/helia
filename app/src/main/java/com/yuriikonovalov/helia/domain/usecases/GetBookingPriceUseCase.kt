package com.yuriikonovalov.helia.domain.usecases

import android.util.Log
import com.yuriikonovalov.helia.domain.repositories.HotelRepository
import com.yuriikonovalov.helia.utils.Result
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

interface GetBookingPriceUseCase {
    suspend operator fun invoke(hotelId: String, from: LocalDate, to: LocalDate): Result<Double>
}

class GetBookingPriceUseCaseImpl @Inject constructor(
    private val hotelRepository: HotelRepository
) : GetBookingPriceUseCase {
    override suspend fun invoke(hotelId: String, from: LocalDate, to: LocalDate): Result<Double> {
        return try {
            val days = ChronoUnit.DAYS.between(from.atStartOfDay(), to.atStartOfDay())
            Log.d("GetBookingPriceUseCase", "days: $days")
            val pricePerNight = hotelRepository.getHotelDetails(hotelId).hotel.price
            val price = pricePerNight * days
            Result.Success(price)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
}
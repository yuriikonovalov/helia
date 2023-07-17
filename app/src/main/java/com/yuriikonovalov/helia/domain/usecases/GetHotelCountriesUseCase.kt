package com.yuriikonovalov.helia.domain.usecases

import com.yuriikonovalov.helia.domain.repositories.HotelRepository
import com.yuriikonovalov.helia.utils.Result
import javax.inject.Inject

interface GetHotelCountriesUseCase {
    suspend operator fun invoke(): Result<List<String>>
}

class GetHotelCountriesUseCaseImpl @Inject constructor(
    private val hotelRepository: HotelRepository
) : GetHotelCountriesUseCase {
    override suspend fun invoke(): Result<List<String>> {
        return try {
            val countries = hotelRepository.getCountries()
            Result.Success(countries)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
}
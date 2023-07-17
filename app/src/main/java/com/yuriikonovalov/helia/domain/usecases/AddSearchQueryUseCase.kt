package com.yuriikonovalov.helia.domain.usecases

import com.yuriikonovalov.helia.domain.repositories.SearchQueryRepository
import com.yuriikonovalov.helia.utils.Result
import javax.inject.Inject

interface AddSearchQueryUseCase {
    suspend operator fun invoke(text: String): Result<Unit>
}

class AddSearchQueryUseCaseImpl @Inject constructor(
    private val searchQueryRepository: SearchQueryRepository
) : AddSearchQueryUseCase {
    override suspend fun invoke(text: String): Result<Unit> {
        return try {
            searchQueryRepository.addSearchQuery(text)
            Result.Success(Unit)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
}
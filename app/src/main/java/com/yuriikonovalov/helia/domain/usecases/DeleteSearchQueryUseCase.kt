package com.yuriikonovalov.helia.domain.usecases

import com.yuriikonovalov.helia.domain.repositories.SearchQueryRepository
import com.yuriikonovalov.helia.utils.Result
import javax.inject.Inject

interface DeleteSearchQueryUseCase {
    suspend operator fun invoke(id: String): Result<Unit>
}

class DeleteSearchQueryUseCaseImpl @Inject constructor(
    private val searchQueryRepository: SearchQueryRepository
) : DeleteSearchQueryUseCase {
    override suspend fun invoke(id: String): Result<Unit> {
        return try {
            searchQueryRepository.deleteSearchQuery(id)
            Result.Success(Unit)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
}
package com.yuriikonovalov.helia.domain.usecases

import com.yuriikonovalov.helia.domain.entities.SearchQuery
import com.yuriikonovalov.helia.domain.repositories.SearchQueryRepository
import com.yuriikonovalov.helia.utils.Result
import com.yuriikonovalov.helia.utils.asResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetSearchQueriesUseCase {
    operator fun invoke(): Flow<Result<List<SearchQuery>>>
}

class GetSearchQueriesUseCaseImpl @Inject constructor(
    private val searchQueryRepository: SearchQueryRepository
) : GetSearchQueriesUseCase {
    override fun invoke(): Flow<Result<List<SearchQuery>>> {
        return searchQueryRepository.getSearchQueries().asResult()
    }
}
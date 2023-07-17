package com.yuriikonovalov.helia.domain.repositories

import com.yuriikonovalov.helia.domain.entities.SearchQuery
import kotlinx.coroutines.flow.Flow

interface SearchQueryRepository {
    fun getSearchQueries(): Flow<List<SearchQuery>>
    suspend fun deleteSearchQuery(id: String)
    suspend fun addSearchQuery(query: String)
}
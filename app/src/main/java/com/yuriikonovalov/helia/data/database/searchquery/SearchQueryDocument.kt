package com.yuriikonovalov.helia.data.database.searchquery

import com.yuriikonovalov.helia.domain.entities.SearchQuery

data class SearchQueryDocument(
    val id: String = "",
    val text: String = ""
) {
    fun toDomain() = SearchQuery(id = id, text = text)

    companion object {
        fun fromDomain(domain: SearchQuery) = SearchQueryDocument(
            id = domain.id,
            text = domain.text
        )
    }
}

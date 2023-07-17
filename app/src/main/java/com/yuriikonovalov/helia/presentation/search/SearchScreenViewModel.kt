package com.yuriikonovalov.helia.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuriikonovalov.helia.domain.usecases.AddSearchQueryUseCase
import com.yuriikonovalov.helia.domain.usecases.BookmarkHotelUseCase
import com.yuriikonovalov.helia.domain.usecases.DeleteSearchQueryUseCase
import com.yuriikonovalov.helia.domain.usecases.GetHotelCountriesUseCase
import com.yuriikonovalov.helia.domain.usecases.GetSearchQueriesUseCase
import com.yuriikonovalov.helia.domain.usecases.SearchHotelsUseCase
import com.yuriikonovalov.helia.domain.valueobjects.HotelCategory
import com.yuriikonovalov.helia.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val searchHotels: SearchHotelsUseCase,
    private val bookmarkHotel: BookmarkHotelUseCase,
    private val getSearchQueries: GetSearchQueriesUseCase,
    private val deleteSearchQuery: DeleteSearchQueryUseCase,
    private val addSearchQuery: AddSearchQueryUseCase,
    private val getHotelCountries: GetHotelCountriesUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SearchUiState())
    val state = _state.asStateFlow()

    private val searchInvocations = Channel<Unit>()

    init {
        collectSearchResults()
        collectCountries()
        collectRecentSearchQueries()

        // for triggering loading hotels for the first time
        searchInvocations.trySend(Unit)
    }

    private fun collectSearchResults() {
        viewModelScope.launch {
            state
                .map { value -> value.hotelCategory }
                .distinctUntilChanged()
                .combine(searchInvocations.receiveAsFlow()) { state, _ -> state }
                .collectLatest { _ ->
                    _state.update { it.updateIsSearching(true) }
                    searchHotels(
                        query = state.value.searchValue,
                        categories = state.value.hotelCategory.asSearchParam(),
                        filter = state.value.filter
                    ).collect { result ->
                        _state.update { it.updateIsSearching(false) }
                        when (result) {
                            is Result.Error -> _state.update { it.updateHotels(emptyList()) }
                            is Result.Success -> _state.update { it.updateHotels(result.data) }
                        }
                    }
                }
        }
    }


    fun handleIntent(intent: SearchIntent) = when (intent) {
        is SearchIntent.BookmarkHotel -> handleBookmarkHotel(intent.id, intent.isBookmarked)
        is SearchIntent.ChangeDisplayMode -> _state.update { it.updateDisplayMode(intent.displayMode) }
        is SearchIntent.ChangeHotelCategory -> _state.update { it.updateHotelCategory(intent.category) }
        is SearchIntent.DeleteRecentSearchQuery -> handleDeleteRecentSearchQuery(intent.id)
        is SearchIntent.UpdateSearchBarFocus -> _state.update { it.updateIsSearchBarFocused(intent.isFocused) }
        is SearchIntent.UpdateSearchValue -> _state.update { it.updateSearchValue(intent.value) }
        is SearchIntent.ClickCountry -> _state.update { it.updateCountryFilter(intent.country) }
        is SearchIntent.ClickFacility -> _state.update { it.updateFacilityFilter(intent.facility) }
        is SearchIntent.ClickSort -> _state.update { it.updateSort(intent.sort) }
        SearchIntent.ApplyFilter -> handleApplyFilter()
        SearchIntent.ResetFilter -> handleResetFilter()
        SearchIntent.ResetUnappliedFilter -> _state.update { it.resetUnappliedFilter() }
        SearchIntent.Search -> handleSearch()
        is SearchIntent.ChangePriceRange -> _state.update { it.updatePriceFilter(intent.range) }
    }

    private fun handleResetFilter() {
        _state.update { it.resetFilter() }
        searchInvocations.trySend(Unit)
    }

    private fun handleApplyFilter() {
        _state.update { it.applyFilter() }
        searchInvocations.trySend(Unit)
    }

    private fun handleBookmarkHotel(id: String, bookmarked: Boolean) {
        viewModelScope.launch {
            bookmarkHotel(hotelId = id, bookmark = !bookmarked)
        }
    }

    private fun handleDeleteRecentSearchQuery(id: String) {
        viewModelScope.launch {
            deleteSearchQuery(id)
        }
    }

    private fun handleSearch() {
        viewModelScope.launch {
            // remember the search query
            addSearchQuery(state.value.searchValue)
            searchInvocations.send(Unit)
        }
    }

    private fun collectCountries() {
        viewModelScope.launch {
            val result = getHotelCountries()
            when (result) {
                is Result.Error -> _state.update { it.updateCountries(emptyList()) }
                is Result.Success -> _state.update { it.updateCountries(result.data) }
            }
        }
    }


    private fun collectRecentSearchQueries() {
        viewModelScope.launch {
            getSearchQueries().collectLatest { result ->
                when (result) {
                    is Result.Error -> _state.update { it.updateSearchQueries(emptyList()) }
                    is Result.Success -> _state.update { it.updateSearchQueries(result.data) }
                }
            }
        }
    }

    private fun HotelCategory?.asSearchParam(): List<HotelCategory> {
        return if (this != null) {
            listOf(this)
        } else {
            HotelCategory.values().toList()
        }
    }
}
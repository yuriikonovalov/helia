package com.yuriikonovalov.helia.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuriikonovalov.helia.domain.usecases.BookmarkHotelUseCase
import com.yuriikonovalov.helia.domain.usecases.GetRecentlyBookedHotelsUseCase
import com.yuriikonovalov.helia.domain.usecases.GetHotelsByCategoryUseCase
import com.yuriikonovalov.helia.domain.usecases.ObserveUserUseCase
import com.yuriikonovalov.helia.domain.valueobjects.HotelCategory
import com.yuriikonovalov.helia.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val observeUser: ObserveUserUseCase,
    private val getHotelsByCategory: GetHotelsByCategoryUseCase,
    private val bookmarkHotel: BookmarkHotelUseCase,
    private val getRecentlyBookedHotels: GetRecentlyBookedHotelsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    init {
        collectUserName()
        collectRecentlyBookedHotels()
        collectHotelsByCategory()
    }

    fun handleIntent(intent: HomeIntent) = when (intent) {
        is HomeIntent.ChangeHotelCategory -> handleChangeHotelCategory(intent.category)
        is HomeIntent.BookmarkHotel -> handleBookmarkHotel(intent.hotelId, intent.isBookmarked)
    }

    private fun handleBookmarkHotel(hotelId: String, bookmarked: Boolean) {
        viewModelScope.launch {
            bookmarkHotel(
                hotelId = hotelId,
                bookmark = !bookmarked
            )
        }
    }

    private fun handleChangeHotelCategory(category: HotelCategory) {
        _state.update { it.updateHotelCategory(category) }

    }


    private fun collectHotelsByCategory() {
        viewModelScope.launch {
            state
                .map { it.hotelCategory }
                .distinctUntilChanged()
                .collectLatest { hotelCategory ->
                    _state.update { it.updateIsHotelByCategoryLoading(true) }
                    getHotelsByCategory(hotelCategory)
                        .collectLatest { result ->
                            _state.update { it.updateIsHotelByCategoryLoading(false) }

                            when (result) {
                                is Result.Success -> _state.update { it.updateHotels(result.data) }
                                is Result.Error -> {
                                    result.exception?.printStackTrace()
                                    _state.update { it.updateRecentlyBookedHotels(emptyList()) }
                                }
                            }
                        }
                }
        }
    }

    private fun collectUserName() {
        viewModelScope.launch {
            observeUser()
                .filter { it is Result.Success }
                .map { result ->
                    result as Result.Success
                    result.data.fullName
                }
                .collect { name ->
                    _state.update { it.updateUserName(name) }
                }
        }
    }

    private fun collectRecentlyBookedHotels() {
        viewModelScope.launch {
            getRecentlyBookedHotels(limited = true)
                .collectLatest { result ->
                    when (result) {
                        is Result.Success -> _state.update { it.updateRecentlyBookedHotels(result.data) }
                        is Result.Error -> {
                            result.exception?.printStackTrace()
                            _state.update { it.updateRecentlyBookedHotels(emptyList()) }
                        }
                    }
                }
        }
    }
}
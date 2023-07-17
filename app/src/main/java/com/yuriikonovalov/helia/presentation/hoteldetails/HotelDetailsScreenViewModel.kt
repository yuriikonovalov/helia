package com.yuriikonovalov.helia.presentation.hoteldetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuriikonovalov.helia.domain.usecases.BookHotelUseCase
import com.yuriikonovalov.helia.domain.usecases.BookmarkHotelUseCase
import com.yuriikonovalov.helia.domain.usecases.GetHotelDetailsUseCase
import com.yuriikonovalov.helia.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HotelDetailsScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getHotelDetails: GetHotelDetailsUseCase,
    private val bookmarkHotel: BookmarkHotelUseCase,
    private val bookHotel: BookHotelUseCase
) : ViewModel() {
    private val hotelId: String = checkNotNull(savedStateHandle[HotelDetailsNavigation.ARGUMENT])
    private val _state = MutableStateFlow<HotelDetailsUiState>(HotelDetailsUiState.Loading)
    val state = _state.asStateFlow()

    init {
        collectHotelDetails()
    }

    fun handleIntent(intent: HotelDetailsIntent) = when (intent) {
        is HotelDetailsIntent.ClickBookmark -> handleClickBookmark(
            intent.hotelId,
            intent.isBookmarked
        )

        is HotelDetailsIntent.BookHotel -> handleBookHotel(intent.hotelId)
    }

    private fun handleBookHotel(hotelId: String) {
        viewModelScope.launch {
            val result = bookHotel(hotelId)
            when (result) {
                is Result.Error -> TODO()
                is Result.Success -> _state.update { value ->
                    (value as HotelDetailsUiState.Success).updateIsHotelBooked(true)
                }
            }
        }
    }

    private fun handleClickBookmark(hotelId: String, bookmarked: Boolean) {
        viewModelScope.launch {
            bookmarkHotel(hotelId = hotelId, bookmark = !bookmarked)
        }
    }

    private fun collectHotelDetails() {
        viewModelScope.launch {
            getHotelDetails(hotelId)
                .collect { result ->
                    when (result) {
                        is Result.Error -> _state.value = HotelDetailsUiState.Error
                        is Result.Success -> _state.value = HotelDetailsUiState.Success(
                            hotelDetails = result.data.details,
                            isBookmarked = result.data.isBookmarked
                        )
                    }
                }
        }
    }
}
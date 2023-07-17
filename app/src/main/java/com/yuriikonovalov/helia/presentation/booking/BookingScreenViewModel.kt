package com.yuriikonovalov.helia.presentation.booking

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuriikonovalov.helia.domain.entities.Hotel
import com.yuriikonovalov.helia.domain.usecases.CancelBookingUseCase
import com.yuriikonovalov.helia.domain.usecases.GetBookedHotelsUseCase
import com.yuriikonovalov.helia.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingScreenViewModel @Inject constructor(
    private val getBookedHotels: GetBookedHotelsUseCase,
    private val cancelBooking: CancelBookingUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(BookingScreenUiState())
    val state = _state.asStateFlow()

    init {
        collectBookedHotels()
    }

    fun handleIntent(intent: BookingScreenIntent) = when (intent) {
        is BookingScreenIntent.ConfirmBookingCanceling -> handleCancelBooking(intent.hotel)
        is BookingScreenIntent.ChangeBookingStatusFiler -> _state.update {
            it.updateBookingStatusFilter(intent.status)
        }

        is BookingScreenIntent.CancelBooking -> _state.update { it.updateHotelToCancel(intent.hotel) }
        BookingScreenIntent.HideBottomSheet -> _state.update { it.updateHotelToCancel(null) }
    }

    private fun handleCancelBooking(hotel: Hotel) {
        viewModelScope.launch {
            _state.update { it.updateHotelToCancel(null) }
            _state.update { it.updateIsLoading(true) }
            val result = cancelBooking(hotel.id)
            _state.update { it.updateIsLoading(false) }
        }
    }

    private fun collectBookedHotels() {
        viewModelScope.launch {
            _state.update { it.updateIsLoading(true) }
            getBookedHotels()
                .collect { result ->
                    Log.d("BookingScreenViewModel", "$result")
                    _state.update { it.updateIsLoading(false) }
                    when (result) {
                        is Result.Error -> _state.update { it.updateHotels(emptyList()) }
                        is Result.Success -> _state.update { it.updateHotels(result.data) }
                    }
                }
        }
    }
}
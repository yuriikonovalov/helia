package com.yuriikonovalov.helia.presentation.hoteldetails.booking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.yuriikonovalov.helia.domain.usecases.GetBookingPriceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getBookingPrice: GetBookingPriceUseCase
) : ViewModel() {
    private val hotelId: String = checkNotNull(savedStateHandle[BookingNavigation.ARGUMENT])


}
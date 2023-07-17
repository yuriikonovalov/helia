package com.yuriikonovalov.helia.presentation.booking

import com.yuriikonovalov.helia.domain.entities.Hotel
import com.yuriikonovalov.helia.domain.valueobjects.BookingStatus

sealed interface BookingScreenIntent {
    data class ConfirmBookingCanceling(val hotel: Hotel) : BookingScreenIntent
    data class ChangeBookingStatusFiler(val status: BookingStatus) : BookingScreenIntent
    data class CancelBooking(val hotel: Hotel) : BookingScreenIntent
    data object HideBottomSheet : BookingScreenIntent
}
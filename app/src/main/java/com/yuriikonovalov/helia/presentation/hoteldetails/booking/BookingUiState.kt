package com.yuriikonovalov.helia.presentation.hoteldetails.booking

import com.yuriikonovalov.helia.domain.entities.HotelSummary
import com.yuriikonovalov.helia.domain.entities.PaymentMethod
import java.time.LocalDate

data class BookingUiState(
    val hotel: HotelSummary? = null,
    val dateFrom: LocalDate = LocalDate.now(),
    val dateTo: LocalDate = LocalDate.now().plusDays(1),
    val guests: Int = 1,
    val price: Double = 0.0,
    val userCards: List<PaymentMethod.Card> = emptyList(),
    val paymentMethod: PaymentMethod? = null,
    val stage: Stage = Stage.DATE
) {

    val maxGuests // TODO: make number of guests == 2 * number of rooms
        get() = 6


    enum class Stage {
        DATE, NAME_OF_RESERVATION, PAYMENT, PAYMENT_CONFIRM, PAYMENT_SUCCESSFUL
    }

}

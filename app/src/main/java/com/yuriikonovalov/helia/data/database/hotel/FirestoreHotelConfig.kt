package com.yuriikonovalov.helia.data.database.hotel

object FirestoreHotelConfig {
    object Collections {

        object BookedHotels {
            const val ROOT = "booked_hotels"
            const val BOOKINGS = "hotel_ids"
        }

        object BookmarkedHotels {
            const val ROOT = "bookmarked_hotels"
            const val HOTEL_IDS = "hotel_ids"
        }
    }
}
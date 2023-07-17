package com.yuriikonovalov.helia.data.database.hotel

import android.util.Log
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.yuriikonovalov.helia.data.database.hotel.model.BookingDocument
import com.yuriikonovalov.helia.domain.valueobjects.BookingStatus
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface HotelDatabaseService {
    fun getBookingDocuments(uid: String): Flow<List<BookingDocument>>
    suspend fun getBookmarkedHotelIds(uid: String): List<String>
    suspend fun addToBooked(uid: String, bookingDocument: BookingDocument)
    suspend fun updateBookedHotelStatus(uid: String, hotelId: String, status: BookingStatus)
    suspend fun addBookmark(uid: String, hotelId: String)
    suspend fun deleteBookmark(uid: String, hotelId: String)
    fun observeBookmarkedHotelIds(uid: String): Flow<List<String>>
}

class FirestoreHotelDatabaseService @Inject constructor(
    private val firestore: FirebaseFirestore
) : HotelDatabaseService {
    override fun getBookingDocuments(uid: String): Flow<List<BookingDocument>> {
//        return firestore
//            .collection(FirestoreHotelConfig.Collections.BookedHotels.ROOT)
//            .document(uid)
//            .get()
//            .await()
//            .toObject<Array<BookingDocument>>()
//            ?.asList()
//            ?: emptyList()


        val ref = firestore
            .collection(FirestoreHotelConfig.Collections.BookedHotels.ROOT)
            .document(uid)
            .collection(FirestoreHotelConfig.Collections.BookedHotels.BOOKINGS)

        return callbackFlow {
            val listener = ref.addSnapshotListener { value, error ->
                if (value != null) {
                    val data = value.toObjects(BookingDocument::class.java)
                    trySend(data)
                } else {
                    trySend(emptyList())
                }

            }

            awaitClose {
                listener.remove()
            }
        }
    }

    override suspend fun getBookmarkedHotelIds(uid: String): List<String> {
        val bookings = firestore
            .collection(FirestoreHotelConfig.Collections.BookmarkedHotels.ROOT)
            .document(uid)
            .collection(FirestoreHotelConfig.Collections.BookedHotels.BOOKINGS)
            .get()
            .await()

        return bookings
            .toObjects(BookingDocument::class.java)
            .map { it.hotelId }
    }

    override suspend fun addToBooked(uid: String, bookingDocument: BookingDocument) {
        firestore.collection(FirestoreHotelConfig.Collections.BookedHotels.ROOT)
            .document(uid)
            .collection(FirestoreHotelConfig.Collections.BookedHotels.BOOKINGS)
            .document()
            .set(bookingDocument)
            .await()
    }

    override suspend fun updateBookedHotelStatus(
        uid: String,
        hotelId: String,
        status: BookingStatus
    ) {
        val data = mapOf("status" to status)
        val snapshot = firestore.collection(FirestoreHotelConfig.Collections.BookedHotels.ROOT)
            .document(uid)
            .collection(FirestoreHotelConfig.Collections.BookedHotels.BOOKINGS)
            .where(Filter.equalTo("hotelId", hotelId))
            .get()
            .await()

        val document = snapshot.documents.first()!!
        val id = document.id

        firestore.collection(FirestoreHotelConfig.Collections.BookedHotels.ROOT)
            .document(uid)
            .collection(FirestoreHotelConfig.Collections.BookedHotels.BOOKINGS)
            .document(id)
            .set(data, SetOptions.merge())
            .await()
    }

    override suspend fun addBookmark(uid: String, hotelId: String) {
        val bookmarksField = FirestoreHotelConfig.Collections.BookmarkedHotels.HOTEL_IDS
        val documentRef = firestore
            .collection(FirestoreHotelConfig.Collections.BookmarkedHotels.ROOT)
            .document(uid)

        firestore.runTransaction { transaction ->
            val bookmarks = transaction.get(documentRef).data?.get(bookmarksField)
            if (bookmarks == null) {
                val data = mapOf(bookmarksField to arrayListOf(hotelId))
                transaction.set(documentRef, data)
            } else {
                transaction.update(documentRef, bookmarksField, FieldValue.arrayUnion(hotelId))
            }
        }.await()
    }

    override suspend fun deleteBookmark(uid: String, hotelId: String) {
        firestore
            .collection(FirestoreHotelConfig.Collections.BookmarkedHotels.ROOT)
            .document(uid)
            .update(
                FirestoreHotelConfig.Collections.BookmarkedHotels.HOTEL_IDS,
                FieldValue.arrayRemove(hotelId)
            )
            .await()
    }

    override fun observeBookmarkedHotelIds(uid: String): Flow<List<String>> {
        val ref = firestore
            .collection(FirestoreHotelConfig.Collections.BookmarkedHotels.ROOT)
            .document(uid)

        return callbackFlow {
            val listener = ref.addSnapshotListener { value, error ->
                val data = value?.data?.get(
                    FirestoreHotelConfig.Collections.BookmarkedHotels.HOTEL_IDS
                )
                if (data != null) {
                    trySend(data as List<String>)
                } else {
                    trySend(emptyList())
                }

            }

            awaitClose {
                listener.remove()
            }
        }

    }

}